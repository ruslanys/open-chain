package io.openfuture.chain.service

import io.openfuture.chain.entity.Block
import io.openfuture.chain.entity.GenesisBlock
import io.openfuture.chain.entity.MainBlock
import io.openfuture.chain.entity.transaction.BaseTransaction
import io.openfuture.chain.entity.transaction.CoinBaseTransaction
import io.openfuture.chain.exception.NotFoundException
import io.openfuture.chain.repository.BlockRepository
import io.openfuture.chain.repository.GenesisBlockRepository
import io.openfuture.chain.repository.MainBlockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultBlockService(
    private val repository: BlockRepository<Block>,
    private val mainBlockRepository: MainBlockRepository,
    private val genesisBlockRepository: GenesisBlockRepository,
    private val transactionService: BaseTransactionService<BaseTransaction>,
    private val coinBaseTransactionService: CoinBaseTransactionService,
    private val walletService: WalletService
) : BlockService {

    @Transactional(readOnly = true)
    override fun get(hash: String): Block = repository.findByHash(hash)
        ?: throw NotFoundException("Block with hash:$hash not found ")


    @Transactional(readOnly = true)
    override fun getLast(): Block =
        repository.findFirstByOrderByHeightDesc()
            ?: throw NotFoundException("Last block not found!")

    @Transactional(readOnly = true)
    override fun getLastMain(): MainBlock =
        mainBlockRepository.findFirstByOrderByHeightDesc()
            ?: throw NotFoundException("Last Main block not found!")

    @Transactional(readOnly = true)
    override fun getLastGenesis(): GenesisBlock =
        genesisBlockRepository.findFirstByOrderByHeightDesc()
            ?: throw NotFoundException("Last Genesis block not exist!")

    override fun getBlocksAfterCurrentHash(hash: String): List<Block>? {
        val block = repository.findByHash(hash)

        block.let { return repository.findByHeightGreaterThan(block!!.height) }
    }

    override fun isExists(hash: String): Boolean = repository.existsByHash(hash)

    @Transactional
    override fun save(block: MainBlock): MainBlock {
        coinBaseTransactionService.save(block.transactions.first() as CoinBaseTransaction)
        val savedBlock = mainBlockRepository.save(block)
        val transactions = block.transactions
        for (transaction in transactions) {
            transactionService.addToBlock(transaction.hash, savedBlock)
            walletService.updateByTransaction(transaction)
        }
        return savedBlock
    }

    @Transactional
    override fun save(block: GenesisBlock): GenesisBlock {
        return genesisBlockRepository.save(block)
    }

}