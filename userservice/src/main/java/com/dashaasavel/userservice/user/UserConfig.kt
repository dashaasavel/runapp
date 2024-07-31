package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.rabbit.UserDeletionNotificator
import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class UserConfig(
    private val jdbcTemplate: JdbcTemplate,
    private val notificator: UserDeletionNotificator
) {
    @Bean
    fun userRepo() = UserDAO(jdbcTemplate)

    @Bean
    fun userToRolesDAO() = UserToRolesDAO(jdbcTemplate)

    @Bean
    fun rolesDAO() = RolesDAO(jdbcTemplate)

    @Bean
    fun userService() = UserService(userRepo(), userToRolesDAO(), rolesDAO(), notificator)

    @Bean
    fun userGrpcService() = UserServiceGrpc(userService())
}