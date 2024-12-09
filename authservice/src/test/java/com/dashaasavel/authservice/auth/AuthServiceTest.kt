package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.AccessTokenError
import com.dashaasavel.authservice.AuthError
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.tokens.AuthTokens
import com.dashaasavel.authservice.tokens.TokensService
import com.dashaasavel.authservice.tokens.refresh.RefreshToken
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.sql.Timestamp

class AuthServiceTest {
    private val user = Fixtures.user

    private val encoder: PasswordEncoder = mock()
    private val tokensService: TokensService = mock {
        on { createAccessToken(any(), any()) } doReturn "accessToken"
        on { createTokenPair(any(), any()) } doReturn AuthTokens("accessToken", "refreshToken")
    }
    private val userService: UserServiceFacade = mock()
    private val authService = AuthService(
        userService, encoder, tokensService
    )

    @Test
    fun `when user auth with incorrect username then exception should be thrown`() {
        whenever(userService.getUser(user.username)) doReturn null

        assertThrowsAuthServiceException(AuthError.USER_DOES_NOT_EXIST) {
            authService.authUser(user.username, user.password)
        }
    }

    @Test
    fun `when user auth with incorrect password then exception should be thrown`() {
        whenever(userService.getUser(any())) doReturn user
        whenever(encoder.matches(any(), any())) doReturn false

        assertThrowsAuthServiceException(AuthError.INCORRECT_PASSWORD) {
            authService.authUser(user.username, user.password)
        }
    }

    @Test
    fun `when user auth with valid credentials then auth should be success`() {
        whenever(userService.getUser(any())) doReturn user
        whenever(encoder.matches(any(), any())) doReturn true

        authService.authUser(user.username, user.password)

        verify(tokensService).createTokenPair(user.id!!, user.username)
    }

    @Test
    fun `when refresh token does not exist then failed`() {
        whenever(tokensService.findByToken(any())) doReturn null

        assertThrowsAuthServiceException(AccessTokenError.REFRESH_TOKEN_NOT_FOUND) {
            authService.refreshAccessToken("nonexistent")
        }
    }

    @Test
    fun `when refresh token is expired then failed`() {
        val token = "token"
        val refreshToken = RefreshToken().apply {
            this.token = token
            this.userId = 1
            this.username = "john111@gmail.com"
            this.expDate = Timestamp(System.currentTimeMillis() - 10_000)
        }
        whenever(tokensService.findByToken(token)) doReturn refreshToken

        assertThrowsAuthServiceException(AccessTokenError.REFRESH_TOKEN_EXPIRED) {
            authService.refreshAccessToken(token)
        }
    }

    @Test
    fun `when refresh token is valid then failed`() {
        val token = "token"
        val refreshToken = RefreshToken().apply {
            this.token = token
            this.userId = 1
            this.username = "john111@gmail.com"
            this.expDate = Timestamp(System.currentTimeMillis() + 10_000)
        }
        whenever(tokensService.findByToken(token)) doReturn refreshToken

        authService.refreshAccessToken(token)

        verify(tokensService).createAccessToken(1, "john111@gmail.com")
    }

}