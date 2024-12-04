package com.dashaasavel.authservice.auth

import com.dashaasavel.openapi.api.AuthApi
import com.dashaasavel.openapi.model.AuthRequest
import com.dashaasavel.openapi.model.AuthResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
): AuthApi {
    override fun authUser(authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val userCredentials = authRequest.userCredentials
        val authTokens = authService.authUser(userCredentials.username, userCredentials.password)

        val authResponse = AuthResponse().apply {
            this.accessToken = authTokens.accessToken
            this.refreshToken = authTokens.refreshToken
        }
        return ResponseEntity.ok(authResponse)
    }
}