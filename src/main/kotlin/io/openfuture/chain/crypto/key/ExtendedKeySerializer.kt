package io.openfuture.chain.crypto.key

import io.openfuture.chain.crypto.domain.ExtendedKey
import io.openfuture.chain.util.Base58
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

/**
 * Component for serialization public and private keys with correct prefixes.
 */
@Component
class ExtendedKeySerializer {

    companion object {
        private val xpub = byteArrayOf(0x04.toByte(), 0x88.toByte(), 0xB2.toByte(), 0x1E.toByte())
        private val xprv = byteArrayOf(0x04.toByte(), 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
    }

    fun serializePublic(extendedKey: ExtendedKey): String {
        return serialize(xpub, extendedKey, extendedKey.ecKey.public)
    }

    fun serializePrivate(extendedKey: ExtendedKey): String {
        return serialize(xprv, extendedKey, extendedKey.ecKey.getPrivate())
    }

    private fun serialize(prefix: ByteArray, extendedKey: ExtendedKey, keyBytes: ByteArray): String {
        val out = ByteArrayOutputStream()
        out.write(prefix)
        out.write(extendedKey.depth and 0xff)
        out.write(extendedKey.parentFingerprint.ushr(24) and 0xff)
        out.write(extendedKey.parentFingerprint.ushr(16) and 0xff)
        out.write(extendedKey.parentFingerprint.ushr(8) and 0xff)
        out.write(extendedKey.parentFingerprint and 0xff)
        out.write(extendedKey.sequence.ushr(24) and 0xff)
        out.write(extendedKey.sequence.ushr(16) and 0xff)
        out.write(extendedKey.sequence.ushr(8) and 0xff)
        out.write(extendedKey.sequence and 0xff)
        out.write(extendedKey.chainCode)

        if (prefix.contentEquals(xprv)) {
            out.write(0x00)
        }

        out.write(keyBytes)
        return Base58.encodeWithChecksum(out.toByteArray())
    }

}