package io.openfuture.chain.network.message.network.address

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class FindAddressesMessageTest {

    private lateinit var message: FindAddressesMessage
    private lateinit var buffer: ByteBuf


    @Before
    fun setup(){
        buffer = createBuffer("")
        message = FindAddressesMessage()
    }

    @Test
    fun writeShouldWriteExactValuesInBuffer() {
        val actualBuffer = Unpooled.buffer()

        message.write(actualBuffer)

        assertThat(actualBuffer).isEqualTo(buffer)
    }

    @Test
    fun readShouldFillEntityWithExactValuesFromBuffer() {
        val actualMessage = FindAddressesMessage::class.java.newInstance()

        actualMessage.read(buffer)

        assertThat(actualMessage).isEqualToComparingFieldByFieldRecursively(message)
    }

    private fun createBuffer(value: String) : ByteBuf = Unpooled.buffer().writeBytes(ByteBufUtil.decodeHexDump((value)))

}