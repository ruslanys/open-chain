package io.openfuture.chain.network.message.core

import io.netty.buffer.ByteBuf
import io.openfuture.chain.core.annotation.NoArgConstructor
import io.openfuture.chain.network.extension.readString
import io.openfuture.chain.network.extension.writeString

@NoArgConstructor
class DelegateTransactionMessage(
    timestamp: Long,
    fee: Long,
    senderAddress: String,
    hash: String,
    senderSignature: String,
    senderPublicKey: String,
    var nodeId: String,
    var delegateHost: String,
    var delegatePort: Int,
    var amount: Long
) : TransactionMessage(timestamp, fee, senderAddress, hash, senderSignature, senderPublicKey) {

    override fun read(buffer: ByteBuf) {
        super.read(buffer)
        nodeId = buffer.readString()
        delegateHost = buffer.readString()
        delegatePort = buffer.readInt()
        amount = buffer.readLong()
    }

    override fun write(buffer: ByteBuf) {
        super.write(buffer)
        buffer.writeString(nodeId)
        buffer.writeString(delegateHost)
        buffer.writeInt(delegatePort)
        buffer.writeLong(amount)
    }

}