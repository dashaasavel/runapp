package com.dashaasavel.runapplib.healthcheck

/**
 * Реест кастомных хелфчеков
 */
class HealthCheckRegistrarImpl : HealthCheckRegistrar {
    var healthChecks: MutableMap<String, HealthCheck> = HashMap()
        private set


    override fun register(name: String, healthCheck: HealthCheck) {
        healthChecks[name] = healthCheck
    }
}