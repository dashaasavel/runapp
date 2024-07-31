package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.AuthServiceGrpc
import com.dashaasavel.userservice.api.Authservice.AuthUser
import com.dashaasavel.userservice.api.Authservice.RegisterUser
import com.dashaasavel.userservice.role.Roles
import io.grpc.stub.StreamObserver

@GrpcService
class AuthServiceGrpc(
    private val authService: AuthService
) : AuthServiceGrpc.AuthServiceImplBase() {
    override fun registerUser(
        request: RegisterUser.Request,
        responseObserver: StreamObserver<RegisterUser.Response>
    ) {
        val username = request.credentials.username
        val password = request.credentials.password

        responseObserver.reply {
            val userId = authService.registerUser(
                username, password, listOf(Roles.USER)
            )
            RegisterUser.Response.newBuilder().setUserId(userId).build()
        }
    }

    override fun authUser(
        request: AuthUser.Request,
        responseObserver: StreamObserver<AuthUser.Response>
    ) {
        val username = request.credentials.username
        val password = request.credentials.password

        responseObserver.reply {
            val jwtToken = authService.authUser(username, password)
            AuthUser.Response.newBuilder().setJwtToken(jwtToken).build()
        }
    }
}