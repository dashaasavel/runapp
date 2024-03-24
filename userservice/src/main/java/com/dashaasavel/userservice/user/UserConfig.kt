package com.dashaasavel.userservice.user

import com.dashaasavel.userservice.role.RolesDAO
import com.dashaasavel.userservice.role.UserToRolesDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class UserConfig(
    private val jdbcTemplate: JdbcTemplate
) {
    @Bean
    open fun userRepo() = UserDAO(jdbcTemplate)

    @Bean
    open fun userToRolesDAO() = UserToRolesDAO(jdbcTemplate)

    @Bean
    open fun rolesDAO() = RolesDAO(jdbcTemplate)

    @Bean
    open fun userService() = UserService(userRepo(), userToRolesDAO(), rolesDAO())
}