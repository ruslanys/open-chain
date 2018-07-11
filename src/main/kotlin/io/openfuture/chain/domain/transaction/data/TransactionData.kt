package io.openfuture.chain.domain.transaction.data

abstract class TransactionData(
    val amount: Long,
    val recipientKey: String,
    val senderKey: String,
    val senderSignature: String
) {

    abstract fun getByteData(): ByteArray

}