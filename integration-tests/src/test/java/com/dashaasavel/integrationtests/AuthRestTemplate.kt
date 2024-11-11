package com.dashaasavel.integrationtests

import com.dashaasavel.openapi.model.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class AuthRestTemplate(
    private val restTemplate: TestRestTemplate
) {
    var hostAndPort: String = ""
    fun refresh(refreshToken: String): String? {
        val body = RefreshAccessTokenRequest().apply {
            this.refreshToken = refreshToken
        }
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val httpRequest = HttpEntity(body, headers)
        val response =
            restTemplate.postForEntity(hostAndPort + "refresh/", httpRequest, RefreshAccessTokenResponse::class.java)
        return response?.body?.assessToken
    }

    fun auth(username: String, password: String): TokenPair? {
        val body = AuthRequest()
        body.userCredentials = UserCredentials().apply {
            this.username = username
            this.password = password
        }
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val httpRequest = HttpEntity(body, headers)
        val response =
            restTemplate.postForEntity(hostAndPort + "auth/", httpRequest, AuthResponse::class.java)

        val responseBody = response.body?: return null
        return TokenPair(responseBody.accessToken, responseBody.refreshToken)
    }

    fun register(firstName: String, username: String, password: String): Int? {
        val body = RegistrationRequest()
        body.userCredentials = UserCredentials().apply {
            this.username = username
            this.password = password
        }
        body.firstName = firstName
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val httpRequest = HttpEntity(body, headers)
        val response =
            restTemplate.postForEntity(hostAndPort + "register/", httpRequest, RegistrationResponse::class.java)
        return response.body?.userId
    }
}

class TokenPair(
    val accessToken: String,
    val privateToken: String
)

