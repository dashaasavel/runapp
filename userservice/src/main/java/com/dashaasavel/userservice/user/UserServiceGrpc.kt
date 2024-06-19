package com.dashaasavel.userservice.user

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.Userservice.*
import com.dashaasavel.userservice.auth.RegistrationService
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.utils.toGrpcUser
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver

@GrpcService
class UserServiceGrpc(
    private val userService: UserService,
    private val registrationService: RegistrationService
) : com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceImplBase() {
    override fun registerUser(
        request: RegisterUser.Request,
        responseObserver: StreamObserver<Empty>
    ) {
        val username = request.username
        val password = request.password

        responseObserver.reply {
            registrationService.registerUser(
                username,
                password,
                listOf(Roles.USER)
            ) // TODO: получать роли из риквеста??
            Empty.getDefaultInstance()
        }
    }

    override fun getUserById(
        request: GetUserById.Request,
        responseObserver: StreamObserver<GetUserById.Response>
    ) {
        val id = request.id

        responseObserver.reply {
            val responseBuilder = GetUserById.Response.newBuilder()
            userService.getUser(id)?.let {
                responseBuilder.user = it.toGrpcUser()
            }
            responseBuilder.build()
        }
    }

    override fun getUserByUsername(
        request: GetUserByUsername.Request,
        responseObserver: StreamObserver<GetUserByUsername.Response>
    ) {
        val username = request.username

        responseObserver.reply {
            val responseBuilder = GetUserByUsername.Response.newBuilder()
            userService.getUserByUsername(username)?.let {
                responseBuilder.user = it.toGrpcUser()
            }
            responseBuilder.build()
        }
    }
}