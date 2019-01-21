package io.openfuture.chain.core.sync

import io.openfuture.chain.consensus.property.ConsensusProperties
import io.openfuture.chain.core.model.entity.Delegate
import io.openfuture.chain.core.model.entity.block.Block
import io.openfuture.chain.core.model.entity.block.GenesisBlock
import io.openfuture.chain.core.model.entity.block.MainBlock
import io.openfuture.chain.core.model.entity.block.payload.MainBlockPayload
import io.openfuture.chain.core.model.entity.transaction.confirmed.DelegateTransaction
import io.openfuture.chain.core.model.entity.transaction.confirmed.RewardTransaction
import io.openfuture.chain.core.model.entity.transaction.confirmed.TransferTransaction
import io.openfuture.chain.core.model.entity.transaction.confirmed.VoteTransaction
import io.openfuture.chain.core.service.*
import io.openfuture.chain.core.sync.SyncMode.FULL
import io.openfuture.chain.core.sync.SyncStatus.*
import io.openfuture.chain.network.entity.NetworkAddress
import io.openfuture.chain.network.entity.NodeInfo
import io.openfuture.chain.network.message.core.DelegateTransactionMessage
import io.openfuture.chain.network.message.core.RewardTransactionMessage
import io.openfuture.chain.network.message.core.TransferTransactionMessage
import io.openfuture.chain.network.message.core.VoteTransactionMessage
import io.openfuture.chain.network.message.sync.*
import io.openfuture.chain.network.property.NodeProperties
import io.openfuture.chain.network.service.NetworkApiService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.xml.bind.ValidationException

@Component
class ChainSynchronizer(
    private val properties: NodeProperties,
    private val consensusProperties: ConsensusProperties,
    private val blockService: BlockService,
    private val networkApiService: NetworkApiService,
    private val genesisBlockService: GenesisBlockService,
    private val voteTransactionService: VoteTransactionService,
    private val rewardTransactionService: RewardTransactionService,
    private val delegateTransactionService: DelegateTransactionService,
    private val transferTransactionService: TransferTransactionService
) {

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChainSynchronizer::class.java)
    }

    @Volatile
    private var status: SyncStatus = SYNCHRONIZED

    private var syncSession: SyncSession? = null


    fun getStatus(): SyncStatus = status

    @Synchronized
    fun sync() {
        log.debug("Chain in status=$status")
        if (PROCESSING == status) {
            return
        }
        status = PROCESSING

        requestLatestGenesisBlock()
    }

    fun onGenesisBlockResponse(message: GenesisBlockMessage) {
        resetRequestScheduler()
        val nodesInfo = genesisBlockService.getLast().payload.activeDelegates.map { getNodeInfo(it) }.toList()
        try {
            val currentGenesisBlock = GenesisBlock.of(message)
            val lastLocalGenesisBlock = genesisBlockService.getLast()

            if (lastLocalGenesisBlock.height <= currentGenesisBlock.height) {
                syncSession = SyncSession(properties.syncMode!!, lastLocalGenesisBlock, currentGenesisBlock)
                requestEpoch(nodesInfo)
            } else {
                requestLatestGenesisBlock()
            }
        } catch (e: Throwable) {
            log.error(e.message)
            syncFailed()
        }
    }

    fun onEpochResponse(message: EpochResponseMessage) {
        resetRequestScheduler()
        val nodesInfo = genesisBlockService.getLast().payload.activeDelegates.map { getNodeInfo(it) }.toList()
        try {
            if (!message.isEpochExists) {
                requestEpoch(nodesInfo.filter { it.uid != message.nodeId })
                return
            }

            if (syncSession!!.syncMode == FULL &&
                !isValidMerkleRoot(message.mainBlocks) && !isValidTransactions(message.mainBlocks)) {
                requestEpoch(nodesInfo.filter { it.uid != message.nodeId })
                return
            }

            if (!syncSession!!.add(convertToBlocks(message))) {
                requestEpoch(nodesInfo.filter { it.uid != message.nodeId })
                return
            }

            if (!syncSession!!.isCompleted()) {
                requestEpoch(nodesInfo)
                return
            }

            saveBlocks()
        } catch (e: Throwable) {
            log.error(e.message)
            syncFailed()
        }
    }

    fun isInSync(block: Block): Boolean {
        val lastBlock = blockService.getLast()
        return isValidHeight(block, lastBlock) && isValidPreviousHash(block, lastBlock)
    }

    private fun convertToBlocks(message: EpochResponseMessage): List<Block> {
        val listBlocks: MutableList<Block> = mutableListOf(GenesisBlock.of(message.genesisBlock!!))
        val mainBlocks = message.mainBlocks.map {
            val mainBlock = MainBlock.of(it)
            mainBlock.payload.rewardTransaction = mutableListOf(RewardTransaction.of(it.rewardTransaction, mainBlock))
            if (syncSession!!.syncMode == FULL) {
                it.voteTransactions.forEach { vTx -> mainBlock.payload.voteTransactions.add(VoteTransaction.of(vTx, mainBlock)) }
                it.delegateTransactions.forEach { dTx -> mainBlock.payload.delegateTransactions.add(DelegateTransaction.of(dTx, mainBlock)) }
                it.transferTransactions.forEach { vTx -> mainBlock.payload.transferTransactions.add(TransferTransaction.of(vTx, mainBlock)) }
            }
            mainBlock
        }
        listBlocks.addAll(mainBlocks)
        return listBlocks
    }

    private fun isValidRewardTransactions(message: RewardTransactionMessage): Boolean = rewardTransactionService.verify(message)

    private fun isValidVoteTransactions(list: List<VoteTransactionMessage>): Boolean = !list
        .any { !voteTransactionService.verify(it) }

    private fun isValidDelegateTransactions(list: List<DelegateTransactionMessage>): Boolean = !list
        .any { !delegateTransactionService.verify(it) }

    private fun isValidTransferTransactions(list: List<TransferTransactionMessage>): Boolean = !list
        .any { !transferTransactionService.verify(it) }

    private fun isValidTransactions(blocks: List<MainBlockMessage>): Boolean {
        try {
            for (block in blocks) {
                if (!isValidRewardTransactions(block.rewardTransaction)) {
                    throw ValidationException("Invalid reward transaction")
                }
                if (!isValidDelegateTransactions(block.delegateTransactions)) {
                    throw ValidationException("Invalid delegate transactions")
                }
                if (!isValidTransferTransactions(block.transferTransactions)) {
                    throw ValidationException("Invalid transfer transactions")
                }
                if (!isValidVoteTransactions(block.voteTransactions)) {
                    throw ValidationException("Invalid vote transactions")
                }
            }
        } catch (e: ValidationException) {
            log.debug("Transactions are invalid, cause: ${e.message}")
            return false
        }
        return true
    }

    private fun isValidMerkleRoot(mainBlocks: List<MainBlockMessage>): Boolean {
        mainBlocks.forEach { block ->
            val hashes = mutableListOf<String>()
            hashes.addAll(block.transferTransactions.map { it.hash })
            hashes.addAll(block.voteTransactions.map { it.hash })
            hashes.addAll(block.delegateTransactions.map { it.hash })
            hashes.add(block.rewardTransaction.hash)
            if (block.merkleHash != MainBlockPayload.calculateMerkleRoot(hashes)) {
                log.debug("MerkleRoot is invalid")
                return false
            }
        }
        return true
    }

    private fun isValidPreviousHash(block: Block, lastBlock: Block): Boolean = block.previousHash == lastBlock.hash

    private fun isValidHeight(block: Block, lastBlock: Block): Boolean = block.height == lastBlock.height + 1

    private fun requestLatestGenesisBlock() {
        val knownActiveDelegates = genesisBlockService.getLast().payload.activeDelegates.map { getNodeInfo(it) }.toList()
        val message = SyncRequestMessage()

        networkApiService.sendToAddress(message, knownActiveDelegates.random())
        startRequestScheduler()
    }

    private fun requestEpoch(listNodeInfo: List<NodeInfo>) {
        val targetEpoch = if (syncSession!!.isEpochSynced()) {
            syncSession!!.getCurrentGenesisBlock().payload.epochIndex
        } else {
            (syncSession!!.getStorage().last() as GenesisBlock).payload.epochIndex - 1
        }

        val message = EpochRequestMessage(targetEpoch, syncSession!!.syncMode)

        networkApiService.sendToAddress(message, listNodeInfo.shuffled().first())
        startRequestScheduler()
    }

    private fun getNodeInfo(delegate: Delegate): NodeInfo = NodeInfo(delegate.nodeId, NetworkAddress(delegate.host, delegate.port))

    private fun saveBlocks() {
        try {
            val lastLocalBlock = blockService.getLast()
            val filteredStorage = syncSession!!.getStorage().filter { it.height > lastLocalBlock.height }

            filteredStorage.asReversed().chunked(consensusProperties.epochHeight!!.plus(1)).forEach {
                blockService.saveChunk(it, syncSession!!.syncMode)
                log.debug("Blocks saved from ${it.first().height} to ${it.last().height}")
            }

            syncSession = null
            status = SYNCHRONIZED
            log.debug("Chain is SYNCHRONIZED")

        } catch (e: Throwable) {
            log.error("Save block is failed: $e")
            syncFailed()
        }
    }

    private fun syncFailed() {
        syncSession = null
        status = NOT_SYNCHRONIZED
        log.debug("Sync is failed")
    }

    private fun startRequestScheduler() {
        if (future == null || future!!.isDone) {
            future = executor.scheduleWithFixedDelay(
                { expired() },
                properties.syncExpiry!!,
                properties.syncExpiry!!,
                TimeUnit.MILLISECONDS)
        }
    }

    private fun resetRequestScheduler() = future?.cancel(true)

    private fun expired() {
        if (null == syncSession) {
            requestLatestGenesisBlock()
        } else {
            requestEpoch(genesisBlockService.getLast().payload.activeDelegates.map { getNodeInfo(it) }.toList())
        }
    }
}