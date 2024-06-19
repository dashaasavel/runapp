package com.dashaasavel.runapplib.healthcheck

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class HealthCheckAutoConfiguration {
    @Bean
    open fun healthCheckController() = HealthCheckController(jsonStatusManager())

    @Bean
    open fun healthCheckRegistryImpl() = HealthCheckRegistrarImpl()

    @Bean
    open fun jsonStatusManager() = JsonStatusManager(healthCheckRegistryImpl())

    @Bean
    open fun healthCheckRunner(healthCheckConfigurers: List<HealthCheckConfigurer>) = HealthCheckRunner(healthCheckConfigurers, healthCheckRegistryImpl())
}