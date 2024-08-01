package com.dashaasavel.metricaggregator.metric

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class MetricConfig(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Bean
    fun metricDAO() = MetricDAO(jdbcTemplate)

    @Bean
    fun metricService() = MetricService(metricDAO())
}