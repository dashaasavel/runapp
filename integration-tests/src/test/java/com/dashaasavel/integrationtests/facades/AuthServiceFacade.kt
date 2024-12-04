package com.dashaasavel.integrationtests.facades

import com.dashaasavel.integrationtests.AuthRestTemplate
import com.dashaasavel.integrationtests.TokenPair
import java.util.*

class AuthServiceFacade(
    private val authRestTemplate: AuthRestTemplate
) {
    fun registerUser(): Int? {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"
        return registerUser(username, password)
    }

    fun registerUser(username: String, password: String): Int? {
        return authRestTemplate.register("Test", username, password)
    }

    fun refreshAccessToken(refreshToken: String): String? {
        return authRestTemplate.refresh(refreshToken)
    }

    fun authUser(username: String, password: String): TokenPair? {
        return authRestTemplate.auth(username, password)
    }
}