package com.dashaasavel.userservice.auth.security

import com.dashaasavel.userservice.ProfilesHelper
import com.dashaasavel.userservice.auth.RegistrationService
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.auth.mail.MailService
import com.dashaasavel.userservice.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
open class SecurityConfig(
    private val userService: UserService,
    private val mailService: MailSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val profilesHelper: ProfilesHelper
) {
    @Bean
    open fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    open fun registrationService() = RegistrationService(userService, mailService, profilesHelper, confirmationTokenService, passwordEncoder())
}