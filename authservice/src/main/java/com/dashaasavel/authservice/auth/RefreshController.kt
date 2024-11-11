package com.dashaasavel.authservice.auth

import com.dashaasavel.openapi.api.RefreshApi
import com.dashaasavel.openapi.model.RefreshAccessTokenRequest
import com.dashaasavel.openapi.model.RefreshAccessTokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class RefreshController(
    private val authService: AuthService
): RefreshApi {
    override fun refreshToken(request: RefreshAccessTokenRequest): ResponseEntity<RefreshAccessTokenResponse> {
        val refreshToken = request.refreshToken
        val accessToken = authService.refreshAccessToken(refreshToken)
        val response = RefreshAccessTokenResponse().apply {
            this.assessToken = accessToken
        }
        return ResponseEntity.ok(response)
    }
}