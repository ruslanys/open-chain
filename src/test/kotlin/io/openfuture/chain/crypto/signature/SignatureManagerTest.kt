package io.openfuture.chain.crypto.signature

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class SignatureManagerTest {

    @Test
    fun signShouldReturnBase64EncodedSignature() {
        val data = "hello".toByteArray()
        val privateKey = Base64.getDecoder().decode("SwPW/DQEVbNj9RAgrT7MpPCFAoDPQ2xwxyeSP220bD4=")
        val expSign = "MEQCIEvAUWAsicWC7NNwkA6FS8FTUIV9P2WBxDS8JUzu3enjAiAftA9QcMFGBGZsyuUpXld9MPKw7jz3OweQnpa3G/WfuQ=="

        val signature = SignatureManager.sign(data, privateKey)

        assertThat(signature).isEqualTo(expSign)
    }

    @Test
    fun verifyShouldReturnTrueWhenValidSignature() {
        val data = "hello".toByteArray()
        val privateKey = Base64.getDecoder().decode("SwPW/DQEVbNj9RAgrT7MpPCFAoDPQ2xwxyeSP220bD4=")
        val publicKey = Base64.getDecoder().decode("A8vKqcmMh3oml30AglyVaiOOjd370yLM5PdLC1vWrOSn")

        val signature = SignatureManager.sign(data, privateKey)
        val verify = SignatureManager.verify(data, signature, publicKey)

        assertThat(verify).isTrue()
    }

    @Test
    fun verifyShouldReturnFalseWhenInvalidSignature() {
        val data = "hello".toByteArray()
        val privateKey = Base64.getDecoder().decode("DwPW/DQEVbNj2RAgrT7MpPCFAoDPQ2xwxyeSP220bD4=")
        val publicKey = Base64.getDecoder().decode("A8vKqcmMh3oml30AglyVaiOOjd370yLM5PdLC1vWrOSn")

        val signature = SignatureManager.sign(data, privateKey)
        val verify = SignatureManager.verify(data, signature, publicKey)

        assertThat(verify).isFalse()
    }

}