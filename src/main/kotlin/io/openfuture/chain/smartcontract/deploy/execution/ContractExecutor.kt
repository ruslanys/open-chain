package io.openfuture.chain.smartcontract.deploy.execution

import io.openfuture.chain.smartcontract.component.SmartContractInjector
import io.openfuture.chain.smartcontract.component.load.SmartContractLoader
import io.openfuture.chain.smartcontract.deploy.domain.ContractDto
import io.openfuture.chain.smartcontract.deploy.domain.ContractMethod
import io.openfuture.chain.smartcontract.deploy.exception.ContractExecutionException
import io.openfuture.chain.smartcontract.deploy.exception.ContractLoadingException
import io.openfuture.chain.smartcontract.model.SmartContract
import io.openfuture.chain.smartcontract.property.ContractProperties
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.atomic.AtomicInteger


@Component
class ContractExecutor(
    private val properties: ContractProperties
) {

    companion object {
        private val uniqueIdentifier = AtomicInteger(0)
    }

    private var startTime: Long = 0
    private var threadId: Long = 0
    private val pool = Executors.newSingleThreadExecutor()
    private val threadBean = ManagementFactory.getThreadMXBean()
    private val millisecond = 1000000.0
    private val classLoader = SmartContractLoader()


    fun run(contract: ContractDto, method: ContractMethod, executionFee: Long): ExecutionResult {
        val availableTime = executionFee / properties.millisecondCost!!
        val executionTime = Math.min(availableTime, properties.maxExecutionTime!!)
        val instance = loadClassAndState(contract)
        val threadName = "${instance::class.java.simpleName}-${uniqueIdentifier.getAndIncrement()}"
        val result = ExecutionResult(threadName, instance as SmartContract, null)

        val task = executeMethod(instance, method, threadName)
        try {
            result.output = task.get(executionTime, MILLISECONDS)
            val endTime = threadBean.getThreadCpuTime(threadId)

            val actualExecutionTime = Math.ceil((endTime - startTime) / millisecond).toInt()
            result.surplus = (availableTime - actualExecutionTime) * properties.millisecondCost!!
            startTime = endTime
            return result
        } catch (ex: Exception) {
            task.cancel(true)
            throw ContractExecutionException(ex.message, ex)
        }
    }

    private fun executeMethod(instance: SmartContract, method: ContractMethod, identifier: String): Future<Any> {
        return pool.submit(Callable {
            threadId = Thread.currentThread().id
            Thread.currentThread().name = identifier
            val paramTypes = instance.javaClass.declaredMethods.firstOrNull { it.name == method.name }?.parameterTypes
            return@Callable MethodUtils.invokeExactMethod(instance, method.name, method.params, paramTypes)
        })
    }

    private fun loadClassAndState(contract: ContractDto): Any {
        try {
            val clazz = classLoader.loadClass(contract.bytes)

            val inst = SmartContractInjector.initSmartContract(clazz, contract.owner, contract.address)
            return inst
        } catch (ex: Throwable) {
            throw ContractLoadingException("Error while loading contract and state: ${ex.message}", ex)
        }
    }

    data class ExecutionResult(
        var identifier: String,
        var instance: SmartContract?,
        var output: Any?,
        var surplus: Long = 0
    )

}