package io.openfuture.chain.network.base

import io.netty.channel.ChannelHandlerContext
import io.openfuture.chain.network.domain.Peer
import io.openfuture.chain.property.NodeProperties
import io.openfuture.chain.protocol.CommunicationProtocol
import io.openfuture.chain.protocol.CommunicationProtocol.*
import io.openfuture.chain.service.NetworkService
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class PeerRecognitionHandler(
    private val networkService: NetworkService,
    private val properties: NodeProperties
) : BaseHandler(Type.PEER_RECOGNITION) {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val message = Packet.newBuilder()
            .setType(Type.PEER_RECOGNITION)
            .setPeerRecognition(PeerRecognition.newBuilder()
                .setPeer(CommunicationProtocol.Peer.newBuilder()
                    .setHost(properties.host)
                    .setPort(properties.port!!))
                .build())
            .build()
        ctx.writeAndFlush(message)

        ctx.fireChannelActive()
    }

    override fun packetReceived(ctx: ChannelHandlerContext, message: CommunicationProtocol.Packet) {
        val messagePeer = message.peerRecognition.peer
        val peer = Peer(messagePeer.host, messagePeer.port)
        networkService.addPeer(ctx.channel(), peer)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        networkService.removePeer(ctx.channel())
        ctx.fireChannelInactive()
    }

}