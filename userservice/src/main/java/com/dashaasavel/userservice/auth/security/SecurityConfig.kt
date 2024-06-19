package com.dashaasavel.userservice.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
open class SecurityConfig {
    @Bean
    open fun passwordEncoder() = BCryptPasswordEncoder()
}