package com.dashaasavel.userservice.auth

import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.jwt.JwtManager
import com.dashaasavel.userservice.rabbit.RegistrationMessageSender
import com.dashaasavel.userservice.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthConfig(
    private val userService: UserService,
    private val tokenSender: RegistrationMessageSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val confirmationProperties: ConfirmationProperties,
    private val passwordEncoder: PasswordEncoder,
    private val jwtManager: JwtManager,
) {
    @Bean
    fun authService() =
        AuthService(
            userService, confirmationTokenService, confirmationProperties, tokenSender, passwordEncoder, jwtManager
        )

    @Bean
    fun authServiceGrpc() = AuthServiceGrpc(authService())
}