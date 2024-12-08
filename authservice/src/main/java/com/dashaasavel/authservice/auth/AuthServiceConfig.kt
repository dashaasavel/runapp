package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.registration.RegistrationService
import com.dashaasavel.authservice.tokens.TokensService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthServiceConfig(
    private val userService: UserServiceFacade,
    private val tokensService: TokensService
) {
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authService() = AuthService(userService, passwordEncoder(), tokensService)

    @Bean
    fun registrationService() = RegistrationService(userService, passwordEncoder())
}