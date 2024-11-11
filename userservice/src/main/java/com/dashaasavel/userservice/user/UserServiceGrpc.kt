package com.dashaasavel.userservice.user

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.Userservice
import com.dashaasavel.userservice.api.Userservice.GetUser
import com.dashaasavel.userservice.utils.toGrpcUser
import io.grpc.stub.StreamObserver

@GrpcService
class UserServiceGrpc(
    private val userService: UserService,
) : com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceImplBase() {
    override fun getUser(
        request: GetUser.Request,
        responseObserver: StreamObserver<GetUser.Response>
    ) {
        responseObserver.reply {
            val responseBuilder = GetUser.Response.newBuilder()
            val nullableUser = if (request.hasUserId()) {
                val userId = request.userId
                userService.getUser(userId)
            } else if (request.hasUsername()) {
                val username = request.username
                userService.getUser(username)
            } else null
            nullableUser?.let {
                responseBuilder.user = it.toGrpcUser()
            }
            responseBuilder.build()
        }
    }

    override fun saveUser(
        request: Userservice.SaveUser.Request,
        responseObserver: StreamObserver<Userservice.SaveUser.Response>?
    ) {
        val user = User().apply {
            this.firstName = request.firstName
            this.username = request.username
            this.password = request.password
        }
        userService.saveUser(user)
    }
}