package com.dummy.infrastructure

import io.prometheus.client.CollectorRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.CompositeHealth
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.health.Status
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint
import org.springframework.boot.actuate.metrics.export.prometheus.TextOutputFormat
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EndPoints(
    private val metricsScraper: MetricsScraper,
    private val healthEndpoint: HealthEndpoint,
    @Value("\${app.version.number}") private val versionNumber: String,
) {

    @GetMapping(value = ["ping"], produces = ["text/plain"])
    fun ping() = "pong"

    @GetMapping(value = ["metrics"], produces = ["text/plain; version=0.0.4; charset=utf-8"])
    fun metrics() = metricsScraper.scrape(null)

    @GetMapping("ready")
    fun ready() = versionNumber

    @GetMapping(value = ["healthcheck"], produces = ["text/plain"])
    fun health(): String {
        val health = healthEndpoint.health() as CompositeHealth
        return if (Status.UP == health.status) {
            "OK"
        } else {
            val joiner = StringJoiner(System.lineSeparator())
            joiner.add("NOK")
            health.components
                .filter { e -> Status.DOWN == (e.value as Health).status }
                .forEach { e -> joiner.add(e.key + " not available") }
            joiner.toString()
        }
    }

    @GetMapping(value = ["version"], produces = ["text/plain"])
    fun version() = versionNumber

}

@Component
class MetricsScraper(collectorRegistry: CollectorRegistry) : PrometheusScrapeEndpoint(collectorRegistry) {
    fun scrape(includeNames: Set<String>?): String =
        super.scrape(TextOutputFormat.CONTENT_TYPE_004, includeNames).body + System.lineSeparator()
}
