package com.dashaasavel.runservice.api

import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceBlockingStub
import com.dashaasavel.userservice.api.Userservice.IsUserExists

class UserService(
    private val userServiceBlockingStub: UserServiceBlockingStub
) {
    fun isUserExists(userId: Int): Boolean {
        val request = IsUserExists.Request.newBuilder().apply {
            this.userId = userId
        }.build()
        return userServiceBlockingStub.isUserExists(request).isUserExists
    }
}