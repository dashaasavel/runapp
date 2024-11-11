package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.api.User

object Fixtures {
    val user = User().apply {
        this.id = 1
        this.firstName = "firstName"
        this.username = "dashaasavel@gmail.com"
        this.password = "password2"
    }
}