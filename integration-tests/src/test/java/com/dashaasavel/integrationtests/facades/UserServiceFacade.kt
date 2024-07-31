package com.dashaasavel.integrationtests.facades

import com.dashaasavel.userservice.api.UserServiceGrpc
import com.dashaasavel.userservice.api.Userservice

class UserServiceFacade(
    private val userServiceBlockingStub: UserServiceGrpc.UserServiceBlockingStub,
) {
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