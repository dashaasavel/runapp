package com.dashaasavel.userservice.user

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.Userservice.*
import com.dashaasavel.userservice.utils.toGrpcUser
import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.StatusRuntimeException
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

    override fun isUserExists(
        request: IsUserExists.Request,
        responseObserver: StreamObserver<IsUserExists.Response>
    ) {
        responseObserver.reply {
            val isUserExists = if (request.hasUserId()) {
                val userId = request.userId
                userService.isUserExists(userId)
            } else if (request.hasUsername()) {
                val username = request.username
                userService.isUserExists(username)
            } else throw StatusRuntimeException(Status.INVALID_ARGUMENT)
            IsUserExists.Response.newBuilder().setIsUserExists(isUserExists).build()
        }
    }

    override fun deleteUser(
        request: DeleteUser.Request,
        responseObserver: StreamObserver<Empty>
    ) {
        responseObserver.reply {
            if (request.hasUserId()) {
                val userId = request.userId
                userService.deleteUser(userId)
            } else if (request.hasUsername()) {
                val username = request.username
                userService.deleteUser(username)
            }
            Empty.getDefaultInstance()
        }
    }
}