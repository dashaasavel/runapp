package com.dashaasavel.authservice.tokens.refresh

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class RefreshTokenConfig(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Bean
    fun refreshTokenDAO() = RefreshTokenDAO(jdbcTemplate)

    @Bean
    fun refreshTokenService() = RefreshTokenService(refreshTokenDAO(), refreshTokenProperties())

    @Bean
    @ConfigurationProperties("refresh-token")
    fun refreshTokenProperties() = RefreshTokenProperties()
}