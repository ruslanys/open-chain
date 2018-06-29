package io.openfuture.chain.controller

import io.openfuture.chain.domain.crypto.DerivationKeyRequest
import io.openfuture.chain.domain.crypto.MasterKeyRequest
import io.openfuture.chain.domain.crypto.key.AddressKeyDto
import io.openfuture.chain.domain.crypto.key.KeyDto
import io.openfuture.chain.service.CryptoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("${PathConstant.RPC}/keys")
class CryptoController(
    val cryptoService: CryptoService
) {

    @PostMapping("/getMasterKey")
    fun getMasterKey(@RequestBody keyRequest: MasterKeyRequest): KeyDto {
        val key = cryptoService.getMasterKey(keyRequest.seedPhrase)

        return KeyDto(
                cryptoService.getPublicKey(key),
                cryptoService.getPrivateKey(key)
        )
    }

    @PostMapping("/getDerivationKey")
    fun getDerivationKey(@RequestBody keyRequest: DerivationKeyRequest): AddressKeyDto {
        val key = cryptoService.getDerivationKey(keyRequest.seedPhrase, keyRequest.derivationPath)

        return AddressKeyDto(
                cryptoService.getPublicKey(key),
                cryptoService.getPrivateKey(key),
                key.ecKey.getAddress()
        )
    }

}