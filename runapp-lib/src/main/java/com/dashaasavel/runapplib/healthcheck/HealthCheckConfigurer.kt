package com.dashaasavel.runapplib.healthcheck

interface HealthCheckConfigurer {
    fun configureHealthCheck(registry: HealthCheckRegistrar)
}