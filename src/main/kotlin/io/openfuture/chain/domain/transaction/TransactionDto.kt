package io.openfuture.chain.domain.transaction

abstract class TransactionDto(
    var timestamp: Long,
    var amount: Long,
    var recipientKey: String,
    var senderKey: String,
    var senderSignature: String,
    var hash: String
)
