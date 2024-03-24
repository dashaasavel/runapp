package com.dashaasavel.userservice.auth.confirmation

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class ConfirmationConfig(
    private val jdbcTemplate: JdbcTemplate
) {
    @Bean
    @ConfigurationProperties("confirmation")
    open fun confirmationProperties() = ConfirmationProperties()

    @Bean
    open fun confirmationTokenDAO() = ConfirmationTokenDAO(jdbcTemplate)

    @Bean
    open fun confirmationTokenService() = ConfirmationTokenService(confirmationTokenDAO(), confirmationProperties())
}