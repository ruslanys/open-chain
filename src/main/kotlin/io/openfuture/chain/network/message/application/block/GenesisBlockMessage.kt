package io.openfuture.chain.network.message.application.block

import io.netty.buffer.ByteBuf
import io.openfuture.chain.network.annotation.NoArgConstructor
import io.openfuture.chain.network.extension.readList
import io.openfuture.chain.network.extension.writeList
import io.openfuture.chain.network.message.application.delegate.DelegateMessage

@NoArgConstructor
class GenesisBlockMessage(
    height: Long,
    previousHash: String,
    blockTimestamp: Long,
    reward: Long,
    publicKey: String,
    hash: String,
    signature: String,
    var epochIndex: Long,
    var activeDelegates: MutableSet<DelegateMessage>
) : BlockMessage(height, previousHash, blockTimestamp, reward, publicKey, hash, signature) {

    override fun read(buffer: ByteBuf) {
        super.read(buffer)

        epochIndex = buffer.readLong()
        activeDelegates = buffer.readList<DelegateMessage>().toMutableSet()
    }

    override fun write(buffer: ByteBuf) {
        super.write(buffer)

        buffer.writeLong(epochIndex)
        buffer.writeList(activeDelegates.toList())
    }

    override fun toString() = "NetworkGenesisBlock(hash=$hash)"

}
