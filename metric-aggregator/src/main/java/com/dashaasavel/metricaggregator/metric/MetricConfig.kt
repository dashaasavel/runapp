package com.dashaasavel.metricaggregator.metric

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class MetricConfig(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Bean
    open fun metricDAO() = MetricDAO(jdbcTemplate)
}