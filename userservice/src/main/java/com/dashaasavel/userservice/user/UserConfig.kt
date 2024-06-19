package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.ProfilesHelper
import com.dashaasavel.userservice.auth.RegistrationService
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailSender
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
open class UserConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val mailService: MailSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val profilesHelper: ProfilesHelper,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    open fun userRepo() = UserDAO(jdbcTemplate)

    @Bean
    open fun userToRolesDAO() = UserToRolesDAO(jdbcTemplate)

    @Bean
    open fun rolesDAO() = RolesDAO(jdbcTemplate)

    @Bean
    open fun userService() = UserService(userRepo(), userToRolesDAO(), rolesDAO())

    @Bean
    open fun userGrpcService() = UserServiceGrpc(userService(), registrationService())

    @Bean
    open fun registrationService() = RegistrationService(userService(), mailService, profilesHelper, confirmationTokenService, passwordEncoder)
}