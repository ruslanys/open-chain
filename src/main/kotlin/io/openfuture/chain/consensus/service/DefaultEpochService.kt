package io.openfuture.chain.consensus.service

import io.openfuture.chain.consensus.property.ConsensusProperties
import io.openfuture.chain.core.model.entity.Delegate
import io.openfuture.chain.core.service.GenesisBlockService
import io.openfuture.chain.network.component.node.NodeClock
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultEpochService(
    private val genesisBlockService: GenesisBlockService,
    private val properties: ConsensusProperties,
    private val clock: NodeClock
) : EpochService {

    override fun getEpochStart(): Long = genesisBlockService.getLast().timestamp

    override fun getDelegates(): List<Delegate> = genesisBlockService.getLast().payload.activeDelegates

    override fun getEpochIndex(): Long = genesisBlockService.getLast().payload.epochIndex

    override fun getGenesisBlockHeight(): Long = genesisBlockService.getLast().height

    override fun getCurrentSlotOwner(): Delegate {
        val genesisBlock = genesisBlockService.getLast()
        val random = Random(genesisBlock.height + getSlotNumber())
        return genesisBlock.payload.activeDelegates.shuffled(random).first()
    }

    override fun getSlotNumber(): Long = ((clock.networkTime() - getEpochStart()) / fullTimeSlotDuration())

    override fun isInTimeSlot(time: Long): Boolean = (getTimeSlotStart(time) < properties.timeSlotDuration!!)

    override fun timeToNextTimeSlot(time: Long): Long = (fullTimeSlotDuration() - getTimeSlotStart(time))

    override fun getSlotNumber(time: Long): Long = ((time - getEpochStart()) / fullTimeSlotDuration())

    override fun getEpochEndTime(): Long = (getEpochStart() + getSlotNumber() * fullTimeSlotDuration())

    private fun getTimeSlotStart(time: Long): Long = (time - getEpochStart()) % fullTimeSlotDuration()

    private fun fullTimeSlotDuration(): Long = (properties.timeSlotDuration!! + properties.timeSlotInterval!!)
}