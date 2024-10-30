package com.dashaasavel.userservice.auth.token.refresh

import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenDAO
import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenProperties
import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenService
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