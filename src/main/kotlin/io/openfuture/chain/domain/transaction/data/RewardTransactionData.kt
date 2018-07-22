package io.openfuture.chain.domain.transaction.data

class RewardTransactionData(
    amount: Double,
    fee: Double,
    recipientAddress: String,
    senderAddress: String,
    val hashCreatedBlock: String
) : BaseTransactionData(amount, fee, recipientAddress, senderAddress) {

    override fun getBytes(): ByteArray {
        val builder = StringBuilder()
        builder.append(amount)
        builder.append(fee)
        builder.append(recipientAddress)
        builder.append(senderAddress)
        builder.append(hashCreatedBlock)
        return builder.toString().toByteArray()
    }

}