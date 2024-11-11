package com.dashaasavel.authservice.auth

import com.dashaasavel.openapi.api.RegisterApi
import com.dashaasavel.openapi.model.RegistrationRequest
import com.dashaasavel.openapi.model.RegistrationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController

class RegistrationController(
    private val authService: AuthService
): RegisterApi {
    override fun registerUser(request: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val firstName = request.firstName
        val username = request.userCredentials.username
        val password = request.userCredentials.password
        val userId = authService.registerUser(firstName, username, password)
        val response = RegistrationResponse().apply {
            this.userId = userId
        }
        return ResponseEntity.ok(response)
    }
}