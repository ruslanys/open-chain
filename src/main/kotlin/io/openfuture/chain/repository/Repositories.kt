package io.openfuture.chain.repository

import io.openfuture.chain.entity.SeedWord
import io.openfuture.chain.entity.Stakeholder
import io.openfuture.chain.entity.block.Block
import io.openfuture.chain.entity.block.GenesisBlock
import io.openfuture.chain.entity.block.MainBlock
import io.openfuture.chain.entity.peer.Delegate
import io.openfuture.chain.entity.peer.Peer
import io.openfuture.chain.entity.transaction.BaseTransaction
import io.openfuture.chain.entity.transaction.TransferTransaction
import io.openfuture.chain.entity.transaction.VoteTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@NoRepositoryBean
interface BaseRepository<T> : JpaRepository<T, Int>

@Repository
interface BlockRepository<Entity: Block> : BaseRepository<Entity> {

    fun findByHash(hash: String): Entity?

    fun findFirstByOrderByHeightDesc(): Entity?

}

@Repository
interface GenesisBlockRepository : BlockRepository<GenesisBlock>

@Repository
interface MainBlockRepository : BlockRepository<MainBlock>

@Repository
interface BaseTransactionRepository<Entity : BaseTransaction> : BaseRepository<Entity> {

    fun findOneByHash(hash: String): Entity?

    fun findAllByBlockIsNull(): MutableSet<Entity>

}

@Repository
interface TransferTransactionRepository : BaseTransactionRepository<TransferTransaction>

@Repository
interface VoteTransactionRepository : BaseTransactionRepository<VoteTransaction>

@Repository
interface SeedWordRepository : BaseRepository<SeedWord> {

    fun findOneByIndex(index: Int): SeedWord

    fun findOneByValue(value: String): SeedWord?

}

@Repository
interface PeerRepository<Entity : Peer> : BaseRepository<Entity> {

    fun deleteOneByNetworkId(networkId: String)

    fun findOneByNetworkId(networkId: String): Entity?

}

@Repository
interface DelegateRepository : PeerRepository<Delegate>

@Repository
interface StakeholderRepository : BaseRepository<Stakeholder> {

    fun findOneByPublicKey(publicKey: String): Stakeholder?

}
