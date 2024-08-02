package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDAO
import com.dashaasavel.userservice.rabbit.RabbitMessageSender
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class UserConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val transactionTemplate: TransactionTemplate,
    private val confirmationTokenDAO: ConfirmationTokenDAO,
    private val messageSender: RabbitMessageSender
) {
    @Bean
    fun userDAO() = UserDAO(jdbcTemplate)

    @Bean
    fun userToRolesDAO() = UserToRolesDAO(jdbcTemplate)

    @Bean
    fun rolesDAO() = RolesDAO(jdbcTemplate)

    @Bean
    fun userService() =
        UserService(userDAO(), userToRolesDAO(), rolesDAO(), confirmationTokenDAO, transactionTemplate, messageSender, messageSender)

    @Bean
    fun userGrpcService() = UserServiceGrpc(userService())
}