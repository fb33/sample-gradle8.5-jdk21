package com.dummy.infrastructure

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.boot.actuate.health.CompositeHealth
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.health.Status


internal class EndPointsTest : WithAssertions {

    private val mockHealthEndpoint = mockk<HealthEndpoint>()
    private val endpoint = EndPoints(mockk<MetricsScraper>(), mockHealthEndpoint, "1.0")
    private val compositeHealth: CompositeHealth = mockkClass(CompositeHealth::class)

    @Test
    fun `healthcheck should respond OK when service is up`() {
        every { compositeHealth.components } returns
                mapOf(
                    "myAmazingService" to Health.up().build()
                )
        every { compositeHealth.status } returns Status.UP
        every { mockHealthEndpoint.health() } returns compositeHealth

        assertThat(endpoint.health()).containsIgnoringNewLines("OK")
    }

    @Test
    fun `healthcheck should respond NOK when a service is down`() {
        every { compositeHealth.components } returns
                mapOf(
                    "myAmazingService" to Health.down(Exception("Oh no! They killed my amazing service !")).build()
                )
        every { compositeHealth.status } returns Status("DOWN")
        every { mockHealthEndpoint.health() } returns compositeHealth

        assertThat(endpoint.health()).containsIgnoringNewLines("NOK", "myAmazingService not available")
    }
}
