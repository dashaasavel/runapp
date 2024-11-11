package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.UserRegistrationException
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenService
import com.dashaasavel.authservice.tokens.access.AccessTokenService
import com.dashaasavel.runapplib.auth.AuthConstants
import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.nhaarman.mockitokotlin2.*
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

class AuthServiceTest {
    private val user = Fixtures.user

    private val encoder: PasswordEncoder = mock()
    private val accessTokenService: AccessTokenService = mock {
        on { createAccessToken(any(), any()) } doReturn "accessToken"
    }
    private val refreshTokenService: RefreshTokenService = mock {
        on { createRefreshToken(any(), any()) } doReturn "refreshToken"
    }

    private val userService: UserServiceFacade = mock()
    private val authService = AuthService(
        userService, encoder, accessTokenService, refreshTokenService
    )

    @Test
    fun `when user auth with incorrect username then method should throw an exception`() {
        whenever(userService.getUser(user.username)) doReturn null

        assertThrowsAuthException<UserAuthException>(AuthError.USER_DOES_NOT_EXIST) {
            authService.authUser(user.username, user.password)
        }
    }

    @Test
    fun `when user auth with incorrect password then method should throw an exception`() {
        whenever(userService.getUser(any())) doReturn user
        whenever(encoder.matches(any(), any())) doReturn false

        assertThrowsAuthException<UserAuthException>(AuthError.INCORRECT_PASSWORD) {
            authService.authUser(user.username, user.password)
        }
    }

    @Test
    fun `when user auth with valid credentials then auth success`() {
        whenever(userService.getUser(any())) doReturn user
        whenever(encoder.matches(any(), any())) doReturn true

        authService.authUser(user.username, user.password)

        verify(accessTokenService).createAccessToken(user.id!!, user.username)
    }

    @Test
    fun `when register existing account then method should throw an exception`() {
        whenever(userService.doesUserExists(any())) doReturn true

        assertThrows<UserRegistrationException>(UserRegistrationError.USER_EXISTS_AND_CONFIRMED.name) {
            authService.registerUser(user.firstName, user.username, user.password)
        }
    }

    @Test
    fun `when register new user with correct credentials then register success`() {
        whenever(userService.doesUserExists(user.username)) doReturn false
        whenever(encoder.encode(any())) doReturn "encodedPassword"
        whenever(userService.saveUser(any(), any(), any())) doReturn user.id!!

        authService.registerUser(user.firstName, user.username, user.password)
    }

    @Test
    fun `when register with invalid email then method should throw an exception`() {
        user.username = "username"
        whenever(userService.doesUserExists(user.username)) doReturn false
        whenever(userService.saveUser(any(), any(), any())) doReturn user.id!!

        assertThrows<UserRegistrationException>(UserRegistrationError.INVALID_EMAIL.name) {
            authService.registerUser(user.firstName, user.username, user.password)
        }
        user.username = "dashasaavel@gmail.com"
    }
}

inline fun <reified T : StatusRuntimeException> assertThrowsAuthException(error: CommonError, executable: () -> Unit) {
    val trailers = assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[AuthConstants.AUTH_ERROR])
}