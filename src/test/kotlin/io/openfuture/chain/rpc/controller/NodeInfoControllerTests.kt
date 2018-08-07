package io.openfuture.chain.rpc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.chain.config.ControllerTests
import io.openfuture.chain.core.service.HardwareInfoService
import io.openfuture.chain.network.component.node.NodeClock
import io.openfuture.chain.rpc.domain.node.CpuInfo
import io.openfuture.chain.rpc.domain.node.HardwareInfo
import io.openfuture.chain.rpc.domain.node.NetworkInfo
import io.openfuture.chain.rpc.domain.node.RamInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean

@WebFluxTest(NodeInfoController::class)
class NodeInfoControllerTests : ControllerTests() {

    @MockBean
    private lateinit var hardwareInfoService: HardwareInfoService

    @MockBean
    private lateinit var nodeClock: NodeClock


    @Test
    fun getUptimeShouldReturnApplicationUptime() {
        val expectedTimestamp = System.currentTimeMillis()

        given(nodeClock.nodeTime()).willReturn(expectedTimestamp)

        val result = webClient.get().uri("/rpc/info/getUptime")
                .exchange()
                .expectStatus().isOk
                .expectBody(Long::class.java)
                .returnResult().responseBody!!

        assertThat(result).isLessThan(expectedTimestamp)
    }

    @Test
    fun getHardwareInfoShoutReturnHardwareInfo() {
        val expected = HardwareInfo(
            CpuInfo("1", 1L, 1),
            RamInfo(1L, 1L, 2L),
            1L,
            listOf(NetworkInfo("IN", listOf("192.168.1.1")))
        )

        given(hardwareInfoService.getHardwareInfo()).willReturn(expected)

        val result = webClient.get().uri("/rpc/info/getHardwareInfo")
                .exchange()
                .expectStatus().isOk
                .expectBody(HardwareInfo::class.java)
                .returnResult().responseBody!!

        assertThat(ObjectMapper().writeValueAsString(result))
            .isEqualTo(ObjectMapper().writeValueAsString(expected))
    }

}