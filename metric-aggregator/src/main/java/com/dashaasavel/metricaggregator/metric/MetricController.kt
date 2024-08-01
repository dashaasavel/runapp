package com.dashaasavel.metricaggregator.metric

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricController(
    private val metricService: MetricService
) {
    @GetMapping("/send") // fixme оно не работает и я честно не знаю почему :(
    fun getAllMetrics(): String {
        val metrics = metricService.getAllMetrics()
        println("metric_size=$metrics")
        return "Hellew"
    }
}