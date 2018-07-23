package io.openfuture.chain.service.transaction

import io.openfuture.chain.entity.MainBlock
import io.openfuture.chain.entity.transaction.BaseTransaction
import io.openfuture.chain.exception.LogicException
import io.openfuture.chain.exception.NotFoundException
import io.openfuture.chain.repository.BaseTransactionRepository
import io.openfuture.chain.service.BaseTransactionService
import org.springframework.transaction.annotation.Transactional

abstract class DefaultBaseTransactionService<Entity : BaseTransaction>(
    protected val repository: BaseTransactionRepository<Entity>
) : BaseTransactionService<Entity> {

    @Transactional(readOnly = true)
    override fun getAllPending(): MutableSet<Entity> {
        return repository.findAllByBlockIsNull()
    }

    @Transactional(readOnly = true)
    override fun get(hash: String): Entity = repository.findOneByHash(hash)
        ?: throw NotFoundException("Transaction with hash: $hash not exist!")

    @Transactional(readOnly = true)
    override fun getByBlock(block: MainBlock): List<Entity> = repository.findAllByBlock(block)

    @Transactional
    override fun addToBlock(hash: String, block: MainBlock): Entity {
        val persistTransaction = this.get(hash)
        if (null != persistTransaction.block) {
            throw LogicException("Transaction with hash: $hash already belong to block!")
        }

        persistTransaction.block = block
        return repository.save(persistTransaction)
    }

    @Transactional
    override fun save(transaction: Entity): Entity = repository.save(transaction)

}