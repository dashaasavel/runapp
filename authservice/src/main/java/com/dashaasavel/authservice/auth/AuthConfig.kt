package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenService
import com.dashaasavel.authservice.tokens.access.AccessTokenService
import com.dashaasavel.runapplib.auth.AuthorizationServerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthConfig(
    private val userService: UserServiceFacade,
    private val accessTokenService: AccessTokenService,
    private val refreshTokenService: RefreshTokenService
) {
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authorizationServerInterceptor() = AuthorizationServerInterceptor()

    @Bean
    fun authService() =
        AuthService(
            userService, passwordEncoder(), accessTokenService, refreshTokenService
        )
}