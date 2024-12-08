package com.dashaasavel.authservice.tokens

import com.dashaasavel.authservice.tokens.access.AccessTokenProperties
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenDAO
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class TokensConfig(
    private val jdbcTemplate: JdbcTemplate,
) {
    @Bean
    fun refreshTokenDAO() = RefreshTokenDAO(jdbcTemplate)

    @Bean
    @ConfigurationProperties("refresh-token")
    fun refreshTokenProperties() = RefreshTokenProperties()

    @Bean
    @ConfigurationProperties("access-token")
    fun accessTokenProperties() = AccessTokenProperties()

    @Bean
    fun refreshTokenService() = TokensService(refreshTokenDAO(), refreshTokenProperties(), accessTokenProperties())
}