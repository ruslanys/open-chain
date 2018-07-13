package io.openfuture.chain.block.validation

import io.openfuture.chain.config.ServiceTests
import io.openfuture.chain.entity.MainBlock
import io.openfuture.chain.entity.transaction.VoteTransaction
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class MainBlockValidatorTests : ServiceTests() {

    private lateinit var mainBlockValidator: MainBlockValidator

    @Before
    fun setUp() {
        mainBlockValidator = MainBlockValidator()
    }

    @Test
    fun isValidShouldReturnTrue() {
        val block = MainBlock(
            ByteArray(1),
            123,
            "prev_block_hash",
            "63b92f8cdd3e55803d0542467ba2a49b5eb5089efb305d94929c0bdac2dc065f",
            1512345678L,
            mutableListOf(
                VoteTransaction(
                    1500000000L,
                    1000.0,
                    "recipient_key",
                    "recipient_address",
                    "sender_key",
                    "sender_address",
                    "sender_signature",
                    "hash",
                    1,
                    "delegate_host",
                    9999
                ),
                VoteTransaction(
                    1500000001L,
                    1002.0,
                    "recipient_key2",
                    "recipient_address2",
                    "sender_key2",
                    "sender_address2",
                    "sender_signature2",
                    "hash2",
                    2,
                    "delegate_host2",
                    11999
                )
            )
        )

        val isBlockValid = mainBlockValidator.isValid(block)

        Assertions.assertThat(isBlockValid).isTrue()
    }

    @Test
    fun isValidShouldReturnFalse() {
        val block = MainBlock(
            ByteArray(1),
            123,
            "prev_block_hash",
            "0000000000000000000000000000000000000000000000000000000000000000",
            1512345678L,
            mutableListOf(
                VoteTransaction(
                    1500000000L,
                    1000.0,
                    "recipient_key",
                    "recipient_address",
                    "sender_key",
                    "sender_address",
                    "sender_signature",
                    "hash",
                    1,
                    "delegate_host",
                    9999
                ),
                VoteTransaction(
                    1500000001L,
                    1002.0,
                    "recipient_key2",
                    "recipient_address2",
                    "sender_key2",
                    "sender_address2",
                    "sender_signature2",
                    "hash2",
                    2,
                    "delegate_host2",
                    11999
                )
            )
        )

        val isBlockValid = mainBlockValidator.isValid(block)

        Assertions.assertThat(isBlockValid).isFalse()
    }
}