package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils.ERROR_METADATA_KEY
import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceBlockingStub
import com.dashaasavel.userservice.api.Userservice
import com.dashaasavel.userservice.api.Userservice.*
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

/**
 * надо сделать обертку над вызовами во внешний сервис по grpc
 */
class UserServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var userServiceBlockingStub: UserServiceBlockingStub

    @Test
    fun test1() {
        val username = "dashaasavel4@gmail.com"
        val registerUserRequest = Userservice.RegisterUser.Request.newBuilder().apply {
            this.username = username
            this.password = "lol9"
        }.build()
        var registerUserResponse = RegisterUser.Response.getDefaultInstance()
        try {
            registerUserResponse = userServiceBlockingStub.registerUser(registerUserRequest)
        } catch (e: RuntimeException) {
            when(e) {
                is StatusRuntimeException -> {
                    println("----- ERROR -----")
                    println("status: ${e.status.code}, error: ${e.trailers!![ERROR_METADATA_KEY]}")
                }
            }
            throw e
        }

        val userId = registerUserResponse.userId

        val getUserByUsernameRequest = GetUserByUsername.Request.newBuilder().apply {
            this.username = username
        }.build()
        val userByUsername = userServiceBlockingStub.getUserByUsername(getUserByUsernameRequest).user

        val getUserByIdRequest = GetUserById.Request.newBuilder().apply {
            this.userId = userId
        }.build()

        val userById = userServiceBlockingStub.getUserById(getUserByIdRequest).user

        assertEquals(userById, userByUsername)
    }
}