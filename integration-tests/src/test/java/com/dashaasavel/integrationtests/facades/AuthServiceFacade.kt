package com.dashaasavel.integrationtests.facades

import com.dashaasavel.integrationtests.utils.LocalStorage
import com.dashaasavel.userservice.api.AuthServiceGrpc
import com.dashaasavel.userservice.api.Authservice
import com.dashaasavel.userserviceapi.utils.AuthServiceMessageWrapper
import java.util.*

class AuthServiceFacade(
    private val authServiceBlockingStub: AuthServiceGrpc.AuthServiceBlockingStub,
    private val localStorage: LocalStorage
) {
    fun registerAndAuthUser(): Int {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"
        return registerAndAuthUser(username, password)
    }

    fun registerAndAuthUser(username: String, password: String): Int {
        val credentials = Authservice.UserCredentials.newBuilder().apply {
            this.username = username
            this.password = password
        }
        val registerUserRequest = Authservice.RegisterUser.Request.newBuilder()
            .setCredentials(credentials)
            .setFirstName("Test")
            .build()

        val userId = authServiceBlockingStub.registerUser(registerUserRequest).userId

        val authRequest = Authservice.AuthUser.Request.newBuilder().apply {
            this.credentials = AuthServiceMessageWrapper.userCredentials(username, password)
        }.build()

        val jwtToken = authServiceBlockingStub.authUser(authRequest).jwtToken
        localStorage.saveToken(jwtToken)
        return userId
    }
}