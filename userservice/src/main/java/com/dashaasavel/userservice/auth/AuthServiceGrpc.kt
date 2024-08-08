package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.core.reply
import com.dashaasavel.runapplib.grpc.register.GrpcService
import com.dashaasavel.userservice.api.AuthServiceGrpc
import com.dashaasavel.userservice.api.Authservice.*
import com.dashaasavel.userservice.role.Roles
import io.grpc.stub.StreamObserver

@GrpcService
class AuthServiceGrpc(
    private val authService: AuthService,
) : AuthServiceGrpc.AuthServiceImplBase() {
    override fun registerUser(
        request: RegisterUser.Request,
        responseObserver: StreamObserver<RegisterUser.Response>
    ) {
        val firstName = request.firstName
        val username = request.credentials.username
        val password = request.credentials.password

        responseObserver.reply {
            val userId = authService.registerUser(
                firstName, username, password, listOf(Roles.USER)
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
            val authTokens = authService.authUser(username, password)
            AuthUser.Response.newBuilder()
                .setAccessToken(authTokens.accessToken)
                .setRefreshToken(authTokens.refreshToken)
                .build()
        }
    }

    override fun refreshAccessToken(
        request: RefreshAccessToken.Request,
        responseObserver: StreamObserver<RefreshAccessToken.Response>
    ) {
        val refreshToken = request.refreshToken
        responseObserver.reply {
            val accessToken = authService.refreshAccessToken(refreshToken)
            RefreshAccessToken.Response.newBuilder().setAccessToken(accessToken).build()
        }
    }
}