package io.openfuture.chain.network.service

import io.netty.channel.ChannelHandlerContext
import io.openfuture.chain.core.service.CommonBlockService
import io.openfuture.chain.core.service.GenesisBlockService
import io.openfuture.chain.core.service.MainBlockService
import io.openfuture.chain.network.message.core.*
import org.springframework.stereotype.Service

// TODO: call core API here
@Service
class DefaultCoreMessageService(
    private val blockService: CommonBlockService,
    private val genesisBlockService: GenesisBlockService,
    private val mainBlockService: MainBlockService
) : CoreMessageService {

    override fun onNetworkBlockRequest(ctx: ChannelHandlerContext, request: SyncBlockRequestMessage) {
        val blocks = blockService.getBlocksAfterCurrentHash(request.hash)

        blocks?.forEach {
            /*when (it) {
                is MainBlock -> ctx.channel().writeAndFlush(it.to)

                is GenesisBlock -> ctx.channel().writeAndFlush(GenesisBlockMessage(it))
            }*/
        }
    }

    override fun onGenesisBlock(ctx: ChannelHandlerContext, block: GenesisBlockMessage) {
        if (blockService.isExists(block.hash)) {
            return
        }

        //genesisBlockService.add(block)
    }

    override fun onMainBlock(ctx: ChannelHandlerContext, block: MainBlockMessage) {
        if (blockService.isExists(block.hash)) {
            return
        }

        //mainBlockService.add(block)
    }

    override fun onTransferTransaction(ctx: ChannelHandlerContext, tx: TransferTransactionMessage) {}

    override fun onDelegateTransaction(ctx: ChannelHandlerContext, tx: DelegateTransactionMessage) {}

    override fun onVoteTransaction(ctx: ChannelHandlerContext, tx: VoteTransactionMessage) {}

}