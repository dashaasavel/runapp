package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.profiles.ProfilesHelper
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
class UserConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val mailService: MailSender,
    private val confirmationTokenService: ConfirmationTokenService,
    private val profilesHelper: ProfilesHelper,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    fun userRepo() = UserDAO(jdbcTemplate)

    @Bean
    fun userToRolesDAO() = UserToRolesDAO(jdbcTemplate)

    @Bean
    fun rolesDAO() = RolesDAO(jdbcTemplate)

    @Bean
    fun userService() = UserService(userRepo(), userToRolesDAO(), rolesDAO())

    @Bean
    fun userGrpcService() = UserServiceGrpc(userService(), registrationService())

    @Bean
    fun registrationService() = RegistrationService(userService(), mailService, profilesHelper, confirmationTokenService, passwordEncoder)
}