package com.dashaasavel.runapplib.healthcheck

import org.springframework.beans.factory.InitializingBean

/**
 * В перспективе переписать на что-то более модное молодежное от спринга
 */
class HealthCheckRunner(
    private val configurers: List<HealthCheckConfigurer>,
    private val registrar: HealthCheckRegistrar
): InitializingBean {
    override fun afterPropertiesSet() {
        for (configurer in configurers) {
            configurer.configureHealthCheck(registrar)
        }
    }
}