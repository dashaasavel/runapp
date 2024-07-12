package com.dashaasavel.runapplib.healthcheck

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HealthCheckAutoConfiguration {
    @Bean
    fun healthCheckController() = HealthCheckController(jsonStatusManager())

    @Bean
    fun healthCheckRegistryImpl() = HealthCheckRegistrarImpl()

    @Bean
    fun jsonStatusManager() = JsonStatusManager(healthCheckRegistryImpl())

    @Bean
    fun healthCheckRunner(healthCheckConfigurers: List<HealthCheckConfigurer>) = HealthCheckRunner(healthCheckConfigurers, healthCheckRegistryImpl())
}