package com.dashaasavel.userservice.auth

import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.jwt.JwtManager
import com.dashaasavel.userservice.auth.jwt.JwtProperties
import com.dashaasavel.userservice.rabbit.RegistrationMessageSender
import com.dashaasavel.userservice.user.UserService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthConfig(
    private val userService: UserService,
    private val tokenSender: RegistrationMessageSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val confirmationProperties: ConfirmationProperties,
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    @ConfigurationProperties("jwt")
    fun jwtProperties() = JwtProperties()

    @Bean
    fun jwtManager() = JwtManager(jwtProperties())

    @Bean
    fun authService() =
        AuthService(
            userService, confirmationTokenService, confirmationProperties, tokenSender, passwordEncoder(), jwtManager()
        )

    @Bean
    fun authServiceGrpc() = AuthServiceGrpc(authService())
}