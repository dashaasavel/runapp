package com.dashaasavel.runapplib.healthcheck

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(
    private val jsonStatusManager: JsonStatusManager
) {
    @RequestMapping(value = ["/status", "/health"])
    fun status(): String {
        return jsonStatusManager.getJson()
    }
}