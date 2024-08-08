package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.auth.AuthorizationServerInterceptor
import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.token.access.AccessTokenService
import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenService
import com.dashaasavel.userservice.rabbit.RegistrationMessageSender
import com.dashaasavel.userservice.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthConfig(
    private val userService: UserService,
    private val tokenSender: RegistrationMessageSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val confirmationProperties: ConfirmationProperties,
    private val accessTokenService: AccessTokenService,
    private val refreshTokenService: RefreshTokenService
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authorizationServerInterceptor() = AuthorizationServerInterceptor()

    @Bean
    fun authService() =
        AuthService(
            userService, confirmationTokenService, confirmationProperties, tokenSender,
            passwordEncoder(), accessTokenService, refreshTokenService
        )

    @Bean
    fun authServiceGrpc() = AuthServiceGrpc(authService())
}