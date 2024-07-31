package com.dashaasavel.userservice.auth.security

import com.dashaasavel.userservice.auth.AuthService
import com.dashaasavel.userservice.auth.AuthServiceGrpc
import com.dashaasavel.userservice.auth.JwtManager
import com.dashaasavel.userservice.auth.JwtProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.profiles.ProfilesHelper
import com.dashaasavel.userservice.user.UserService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityConfig(
    private val userService: UserService,
    private val mailService: MailSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val profilesHelper: ProfilesHelper,
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
        AuthService(userService, mailService, profilesHelper, confirmationTokenService, passwordEncoder(), jwtManager())

    @Bean
    fun authServiceGrpc() = AuthServiceGrpc(authService())
}