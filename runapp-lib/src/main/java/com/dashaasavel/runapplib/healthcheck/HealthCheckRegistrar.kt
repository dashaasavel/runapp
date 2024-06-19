package com.dashaasavel.runapplib.healthcheck

interface HealthCheckRegistrar {
    fun register(name: String, healthCheck: HealthCheck)
}