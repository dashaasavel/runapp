package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.rabbit.RabbitMessageSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class UserConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val transactionTemplate: TransactionTemplate,
    private val messageSender: RabbitMessageSender
) {
    @Bean
    fun userDAO() = UserDAO(jdbcTemplate)

    @Bean
    fun userService() =
        UserService(userDAO(), transactionTemplate, messageSender, messageSender)

    @Bean
    fun userGrpcService() = UserServiceGrpc(userService())
}