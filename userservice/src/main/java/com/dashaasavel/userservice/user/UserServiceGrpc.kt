package com.dashaasavel.userservice.user

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.Userservice.*
import com.dashaasavel.userservice.auth.RegistrationService
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.utils.toGrpcUser
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import java.lang.RuntimeException

@GrpcService
class UserServiceGrpc(
    private val userService: UserService,
    private val registrationService: RegistrationService
) : com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceImplBase() {
    override fun registerUser(
        request: RegisterUser.Request,
        responseObserver: StreamObserver<RegisterUser.Response>
    ) {
        val username = request.username
        val password = request.password

        responseObserver.reply {
            val userId = registrationService.registerUser(
                username, password, listOf(Roles.USER)
            )
            RegisterUser.Response.newBuilder().setUserId(userId).build()
        }
    }

    override fun getUser(
        request: GetUser.Request,
        responseObserver: StreamObserver<GetUser.Response>
    ) {
        responseObserver.reply {
            val responseBuilder = GetUser.Response.newBuilder()
            val nullableUser = if (request.hasUserId()) {
                val userId = request.userId
                userService.getUser(userId)
            } else if(request.hasUsername()) {
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
            } else throw RuntimeException() // TODO
            IsUserExists.Response.newBuilder().setIsUserExists(isUserExists).build()
        }
    }

    override fun deleteUser(request: DeleteUser.Request,
                            responseObserver: StreamObserver<Empty>
    ) {
        responseObserver.reply {
            if (request.hasUserId()) {
                val userId = request.userId
                userService.deleteUser(userId)
            } else if(request.hasUsername()) {
                val username = request.username
                userService.deleteUser(username)
            }
            Empty.getDefaultInstance()
        }
    }
}