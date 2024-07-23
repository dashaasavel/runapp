package com.dashaasavel.integrationtests.facades

import com.dashaasavel.userservice.api.UserServiceGrpc
import com.dashaasavel.userservice.api.Userservice
import java.util.*

class UserServiceFacade(
    private val userServiceBlockingStub: UserServiceGrpc.UserServiceBlockingStub
) {
    fun registerUser(username: String, password: String): Int {
        val registerUserRequest = Userservice.RegisterUser.Request.newBuilder().apply {
            this.username = username
            this.password = password
        }.build()

        return userServiceBlockingStub.registerUser(registerUserRequest).userId
    }

    fun registerUser(): Int {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"
        return registerUser(username, password)
    }

    fun getUserById(userId: Int): Userservice.User {
        val request = Userservice.GetUser.Request.newBuilder().apply {
            this.userId = userId
        }.build()
        return userServiceBlockingStub.getUser(request).user
    }

    fun getUserByUsername(username: String): Userservice.User {
        val request = Userservice.GetUser.Request.newBuilder().apply {
            this.username = username
        }.build()
        return userServiceBlockingStub.getUser(request).user
    }

    fun deleteUserById(userId: Int) {
        val request = Userservice.DeleteUser.Request.newBuilder().apply {
            this.userId = userId
        }.build()
        userServiceBlockingStub.deleteUser(request)
    }

    fun deleteUserByUsername(username: String) {
        val request = Userservice.DeleteUser.Request.newBuilder().apply {
            this.username = username
        }.build()
        userServiceBlockingStub.deleteUser(request)
    }
}