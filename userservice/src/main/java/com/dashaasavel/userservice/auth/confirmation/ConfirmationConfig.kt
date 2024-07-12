package com.dashaasavel.userservice.auth.confirmation

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class ConfirmationConfig(
    private val jdbcTemplate: JdbcTemplate
) {
    @Bean
    @ConfigurationProperties("confirmation")
    fun confirmationProperties() = ConfirmationProperties()

    @Bean
    fun confirmationTokenDAO() = ConfirmationTokenDAO(jdbcTemplate)

    @Bean
    fun confirmationTokenService() = ConfirmationTokenService(confirmationTokenDAO(), confirmationProperties())
}