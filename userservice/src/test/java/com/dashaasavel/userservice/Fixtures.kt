package com.dashaasavel.userservice

import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User

object Fixtures {
    val user = User().apply {
        this.id = 1
        this.firstName = "firstName"
        this.username = "dashaasavel@gmail.com"
        this.password = "password2"
        this.roles = listOf(Roles.USER)
    }
}