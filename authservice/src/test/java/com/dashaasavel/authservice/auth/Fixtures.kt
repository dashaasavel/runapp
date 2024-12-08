package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.api.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object Fixtures {
    private val encoder = BCryptPasswordEncoder()

    val user = User().apply {
        this.id = 1
        this.firstName = "firstName"
        this.username = "john@gmail.com"
        this.password = "john1234!"
    }

    val userFromDB = User().apply {
        this.id = user.id
        this.firstName = user.firstName
        this.username = "john@gmail.com"
        this.password = encoder.encode("john1234!")
    }
}