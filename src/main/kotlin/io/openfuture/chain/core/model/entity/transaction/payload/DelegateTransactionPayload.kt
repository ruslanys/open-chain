package io.openfuture.chain.core.model.entity.transaction.payload

import io.openfuture.chain.core.util.ByteConstants
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets.UTF_8
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class DelegateTransactionPayload(

    @Column(name = "delegate_key", nullable = false, unique = true)
    var delegateKey: String,

    @Column(name = "delegate_host", nullable = false, unique = true)
    var delegateHost: String,

    @Column(name = "delegate_port", nullable = false, unique = true)
    var delegatePort: Int

) : TransactionPayload {

    override fun getBytes(): ByteArray {
        val buffer = ByteBuffer.allocate(delegateKey.toByteArray(UTF_8).size +
            delegateHost.toByteArray(UTF_8).size + ByteConstants.INT_BYTES)

        buffer.put(delegateKey.toByteArray(UTF_8))
        buffer.put(delegateHost.toByteArray(UTF_8))
        buffer.putInt(delegatePort)
        return buffer.array()
    }

}