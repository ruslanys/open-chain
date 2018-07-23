package io.openfuture.chain.entity.block

import io.openfuture.chain.crypto.signature.SignatureManager
import io.openfuture.chain.crypto.util.HashUtils
import io.openfuture.chain.entity.base.BaseModel
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import javax.persistence.*

@Entity
@Table(name = "blocks")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Block(

    privateKey: ByteArray,

    @Column(name = "height", nullable = false)
    var height: Long,

    @Column(name = "previous_hash", nullable = false)
    var previousHash: String,

    @Column(name = "merkle_hash", nullable = false)
    var merkleHash: String,

    @Column(name = "timestamp", nullable = false)
    var timestamp: Long,

    @Column(name = "typeId", nullable = false)
    var typeId: Int,

    @Column(name = "hash", nullable = false, unique = true)
    var hash: String = ByteUtils.toHexString(HashUtils.doubleSha256((previousHash + merkleHash + timestamp + height).toByteArray())),

    @Column(name = "signature", nullable = false)
    var signature: String = SignatureManager.sign(hash.toByteArray(), privateKey)

) : BaseModel()