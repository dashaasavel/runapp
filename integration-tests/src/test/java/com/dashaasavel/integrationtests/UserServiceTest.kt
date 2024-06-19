package com.dashaasavel.integrationtests

import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceBlockingStub
import com.dashaasavel.userservice.api.Userservice
import org.springframework.beans.factory.annotation.Autowired

/**
 * надо сделать обертку над вызовами во внешний сервис по grpc
 */
class UserServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var userServiceBlockingStub: UserServiceBlockingStub

//    @Test
    fun test1() {
        val request = Userservice.RegisterUser.Request.newBuilder().apply {
            this.password = "lol9"
            this.username = "kek12"
        }.build()
        val response = userServiceBlockingStub.registerUser(request)
        println(response)
    }

//    @Test
    fun test2() {
        val request = Userservice.GetUserById.Request.newBuilder().apply {
            this.id = 24
        }.build()
        val response = userServiceBlockingStub.getUserById(request)
        println(response)
    }
}