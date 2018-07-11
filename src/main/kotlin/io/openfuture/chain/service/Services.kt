package io.openfuture.chain.service

import io.netty.channel.Channel
import io.openfuture.chain.crypto.domain.ECKey
import io.openfuture.chain.crypto.domain.ExtendedKey
import io.openfuture.chain.domain.HardwareInfo
import io.openfuture.chain.domain.crypto.RootAccountDto
import io.openfuture.chain.domain.hardware.CpuInfo
import io.openfuture.chain.domain.hardware.NetworkInfo
import io.openfuture.chain.domain.hardware.RamInfo
import io.openfuture.chain.domain.hardware.StorageInfo
import io.openfuture.chain.entity.Block
import io.openfuture.chain.entity.Transaction
import io.openfuture.chain.network.domain.Peer
import io.openfuture.chain.protocol.CommunicationProtocol
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

interface HardwareInfoService {

    fun getHardwareInfo(): HardwareInfo

    fun getCpuInfo(): CpuInfo

    fun getRamInfo(): RamInfo

    fun getDiskStorageInfo(): List<StorageInfo>

    fun getNetworksInfo(): List<NetworkInfo>

}

interface BlockService {

    fun get(hash: String): Block

    fun getLast(): Block

    fun getLastGenesis(): Block

    fun save(block: Block): Block

}

interface CryptoService {

    fun generateSeedPhrase(): String

    fun generateNewAccount(): RootAccountDto

    fun getRootAccount(seedPhrase: String): RootAccountDto

    fun getDerivationKey(seedPhrase: String, derivationPath: String): ExtendedKey

    fun importKey(key: String): ExtendedKey

    fun importWifKey(wifKey: String): ECKey

    fun serializePublicKey(key: ExtendedKey): String

    fun serializePrivateKey(key: ExtendedKey): String

}

interface ConsensusService {

    fun getCurrentEpochHeight(): Int

    fun isGenesisBlockNeeded(): Boolean

}

interface TransactionService {

    fun save(transaction: Transaction): Transaction

}

interface WalletService {

    fun getBalance(address: String): Double

    fun updateByTransaction(transaction: Transaction)

}

interface NetworkService {

    fun start()

    fun broadcast(packet: CommunicationProtocol.Packet)

    fun isConnected(networkId: String): Boolean

    fun getNetworkId(): String

    fun maintainInboundConnections()

    fun addConnectedPeer(channel: Channel, peer: Peer)

    fun removeConnectedPeer(channel: Channel)

    fun addKnownPeers(peers: List<CommunicationProtocol.Peer>)

    fun getPeerInfo() : Peer

    fun connectedPeers(): Set<Peer>
}