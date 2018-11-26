package io.openfuture.chain.network.handler.network

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState.READER_IDLE
import io.netty.handler.timeout.IdleState.WRITER_IDLE
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.ReadTimeoutException
import io.netty.handler.timeout.WriteTimeoutException
import io.openfuture.chain.network.component.ChannelsHolder
import io.openfuture.chain.network.message.network.HeartBeatMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Sharable
class HeartBeatHandler(
    private val channelsHolder: ChannelsHolder
) : SimpleChannelInboundHandler<HeartBeatMessage>() {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(HeartBeatHandler::class.java)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: HeartBeatMessage) {
        log.info("Received heartbeat message from ${ctx.channel().remoteAddress()}")
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, event: Any) {
        if (event !is IdleStateEvent) {
            super.userEventTriggered(ctx, event)
            return
        }
        log.info("Idle event (${event.state()}) inbound to ${ctx.channel().remoteAddress()}")

        val eventState = event.state()
        if (READER_IDLE == eventState) {
            ctx.close()
        } else if (WRITER_IDLE == eventState && null != channelsHolder.getNodeInfoByChannelId(ctx.channel().id())) {
            ctx.writeAndFlush(HeartBeatMessage())
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is WriteTimeoutException -> {
                ctx.writeAndFlush(HeartBeatMessage())
                    .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
            }
            is ReadTimeoutException -> {
                ctx.close()
            }
            else -> super.exceptionCaught(ctx, cause)
        }
    }

}
