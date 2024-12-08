package com.dashaasavel.authservice.api

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.userservice.api.UserServiceGrpc
import com.dashaasavel.userservice.api.Userservice

class UserServiceFacade(
    private val userServiceGrpc: UserServiceGrpc.UserServiceBlockingStub
) {
    fun getUser(username: String): User? {
        val request = Userservice.GetUser.Request.newBuilder().apply {
            this.username = username
        }.build()
        val responseUser = userServiceGrpc.getUser(request).user
        if (responseUser.isNull()) {
            return null
        }
        return User().apply {
            this.id = responseUser.id // ыыыы
            this.firstName = responseUser.firstName
            this.username = responseUser.username
            this.password = responseUser.password
        }
    }

    fun saveUser(firstName: String, username: String, password: String): Int {
        val request = Userservice.SaveUser.Request.newBuilder().apply {
            this.firstName = firstName
            this.username = username
            this.password = password
        }.build()
        return userServiceGrpc.saveUser(request).userId
    }
}

class User {
    var id: Int? = null
    lateinit var firstName: String
    lateinit var username: String
    lateinit var password: String
}