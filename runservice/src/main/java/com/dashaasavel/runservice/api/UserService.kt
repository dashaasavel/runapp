package com.dashaasavel.runservice.api

import com.dashaasavel.runapplib.auth.AuthConstants
import com.dashaasavel.runapplib.auth.BearerToken
import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceBlockingStub
import com.dashaasavel.userservice.api.Userservice.IsUserExists

class UserService(
    private val userServiceBlockingStub: UserServiceBlockingStub
) {
    fun isUserExists(userId: Int): Boolean {
        val request = IsUserExists.Request.newBuilder().apply {
            this.userId = userId
        }.build()

        return userServiceBlockingStub.withCallCredentials(BearerToken(AuthConstants.JWT_KEY.get())).isUserExists(request).isUserExists
    }
}