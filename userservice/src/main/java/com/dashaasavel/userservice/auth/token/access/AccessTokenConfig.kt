package com.dashaasavel.userservice.auth.token.access

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccessTokenConfig {
    @Bean
    @ConfigurationProperties("access-token")
    fun accessTokenProperties() = AccessTokenProperties()

    @Bean
    fun accessTokenService() = AccessTokenService(accessTokenProperties())
}