package io.openfuture.chain.core.model.entity.transaction.unconfirmed

import io.openfuture.chain.core.model.entity.transaction.TransactionHeader
import io.openfuture.chain.core.model.entity.transaction.payload.TransactionPayload
import io.openfuture.chain.core.model.entity.transaction.payload.TransferTransactionPayload
import io.openfuture.chain.core.util.TransactionUtils
import io.openfuture.chain.network.message.core.TransferTransactionMessage
import io.openfuture.chain.rpc.domain.transaction.request.TransferTransactionRequest
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "u_transfer_transactions")
class UnconfirmedTransferTransaction(
    header: TransactionHeader,
    hash: String,
    senderSignature: String,
    senderPublicKey: String,

    @Embedded
    var payload: TransferTransactionPayload

) : UnconfirmedTransaction(header, hash, senderSignature, senderPublicKey) {

    companion object {
        fun of(dto: TransferTransactionMessage): UnconfirmedTransferTransaction = UnconfirmedTransferTransaction(
            TransactionHeader(dto.timestamp, dto.fee, dto.senderAddress),
            dto.hash,
            dto.senderSignature,
            dto.senderPublicKey,
            TransferTransactionPayload(dto.amount, dto.recipientAddress)
        )

        fun of(request: TransferTransactionRequest): UnconfirmedTransferTransaction = UnconfirmedTransferTransaction(
            TransactionHeader(request.timestamp!!, request.fee!!, request.senderAddress!!),
            TransactionUtils.generateHash(
                request.timestamp!!,
                request.fee!!,
                request.senderAddress!!,
                TransferTransactionPayload(request.amount!!, request.recipientAddress!!)),
            request.senderSignature!!,
            request.senderPublicKey!!,
            TransferTransactionPayload(request.amount!!, request.recipientAddress!!)
        )
    }

    override fun getPayload(): TransactionPayload = payload

    override fun toMessage(): TransferTransactionMessage = TransferTransactionMessage(
        header.timestamp,
        header.fee,
        header.senderAddress,
        hash,
        senderSignature,
        senderPublicKey,
        payload.amount,
        payload.recipientAddress
    )

}