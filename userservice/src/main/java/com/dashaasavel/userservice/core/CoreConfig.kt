package com.dashaasavel.userservice.core

import com.dashaasavel.runapplib.healthcheck.ApplicationHealthCheck
import com.dashaasavel.runapplib.healthcheck.HealthCheckConfigurer
import com.dashaasavel.runapplib.healthcheck.HealthCheckRegistrar
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreConfig(
    private val buildProperties: BuildProperties
): HealthCheckConfigurer {
    @Bean
    fun applicationHealthCheck() = ApplicationHealthCheck(buildProperties)

    override fun configureHealthCheck(registry: HealthCheckRegistrar) {
        registry.register("application", applicationHealthCheck())
    }
}