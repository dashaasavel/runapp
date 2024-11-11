package com.dashaasavel.authservice.auth

import com.dashaasavel.openapi.api.AuthApi
import com.dashaasavel.openapi.model.AuthRequest
import com.dashaasavel.openapi.model.AuthResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController: AuthApi {
    override fun authUser(authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        return super.authUser(authRequest)
    }
}