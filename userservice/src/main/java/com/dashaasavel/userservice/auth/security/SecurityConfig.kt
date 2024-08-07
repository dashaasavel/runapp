package com.dashaasavel.userservice.auth.security

import com.dashaasavel.runapplib.auth.AuthorizationServerInterceptor
import com.dashaasavel.userservice.auth.jwt.JwtManager
import com.dashaasavel.userservice.auth.jwt.JwtProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authorizationServerInterceptor() = AuthorizationServerInterceptor()

    // todo see later
//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http.authorizeHttpRequests {
//            it.requestMatchers("/api/v1/auth/confirm/**").permitAll()
//                .requestMatchers("/docker").permitAll()
//                .anyRequest().authenticated()
//        }
//        return http.build()
//    }
//
//    @Bean
//    fun accessDeniedHandler() =
//        AccessDeniedHandler { _, response, _ ->
//            response.sendRedirect("/access-denied")
//        }

    @Bean
    @ConfigurationProperties("jwt")
    fun jwtProperties() = JwtProperties()

    @Bean
    fun jwtManager() = JwtManager(jwtProperties())
}