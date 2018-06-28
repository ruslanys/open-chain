package io.openfuture.chain.crypto.seed.calculator

import org.bouncycastle.crypto.PBEParametersGenerator
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter
import org.springframework.stereotype.Component

@Component
class BouncyCastlePBKDF2WithHmacSHA256 {

    companion object {
        private const val KEY_SIZE = 512
        private const val ITERATION_COUNT = 2048
    }

    fun hash(chars: CharArray, salt: ByteArray): ByteArray {
        val generator = PKCS5S2ParametersGenerator(SHA512Digest())
        generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(chars), salt, ITERATION_COUNT)
        val key = generator.generateDerivedMacParameters(KEY_SIZE) as KeyParameter
        return key.key
    }

}