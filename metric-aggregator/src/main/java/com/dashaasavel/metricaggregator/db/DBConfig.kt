package com.dashaasavel.metricaggregator.db

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class DBConfig(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Bean
    open fun metricDAO() = MetricDAO(jdbcTemplate)
}