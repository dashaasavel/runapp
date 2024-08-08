package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.auth.AuthConstants
import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.Fixtures
import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDTO
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.token.access.AccessTokenService
import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenService
import com.dashaasavel.userservice.rabbit.RegistrationMessageSender
import com.dashaasavel.userservice.user.UserService
import com.nhaarman.mockitokotlin2.*
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AuthServiceTest {
    private val user = Fixtures.user
    private val userService: UserService = mock {
        on { getUser(user.username!!) } doReturn user
        on { isUserExists(user.username!!) } doReturn true
    }
    private val confirmationTokenService: ConfirmationTokenService = mock()
    private val confirmationProperties = ConfirmationProperties().apply {
        this.minutes = 15
        this.serverUrl = "not-local-host"
    }
    private val messageSender: RegistrationMessageSender = mock {
        on { sendConfirmationToken(any(), any(), any(), any(), any()) } doAnswer {}
    }
    private val encoder: PasswordEncoder = mock()
    private val accessTokenService: AccessTokenService = mock {
        on { createAccessToken(any(), any()) } doReturn "accessToken"
    }
    private val refreshTokenService: RefreshTokenService = mock {
        on { createRefreshToken(any(), any()) } doReturn "refreshToken"
    }
    private val authService = AuthService(
        userService, confirmationTokenService, confirmationProperties, messageSender,
        encoder, accessTokenService, refreshTokenService
    )

    @Test
    fun `auth user with incorrect username should throw an exception`() {
        whenever(userService.getUser(user.username!!)) doReturn null

        assertThrowsAuthException<UserAuthException>(AuthError.USER_DOES_NOT_EXIST) {
            authService.authUser(user.username!!, user.password!!)
        }
    }

    @Test
    fun `auth user with incorrect password should throw an exception`() {
        whenever(encoder.matches(any(), any())) doReturn false

        assertThrowsAuthException<UserAuthException>(AuthError.INCORRECT_PASSWORD) {
            authService.authUser(user.username!!, user.password!!)
        }
    }

    @Test
    fun `auth user with valid credentials should be ok`() {
        whenever(encoder.matches(any(), any())) doReturn true

        authService.authUser(user.username!!, user.password!!)

        verify(accessTokenService).createAccessToken(user.id!!, user.username!!)
    }

    @Test
    fun `register existing and confirmed account should throw an exception`() {
        user.confirmed = true

        assertThrows<UserRegistrationException>(UserRegistrationError.USER_EXISTS_AND_CONFIRMED.name) {
            authService.registerUser(user.firstName!!, user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `register existing and not confirmed by active token account should throw an exception`() {
        user.confirmed = false

        val confirmationTokenDTO = ConfirmationTokenDTO().apply {
            this.expirationDate = LocalDateTime.MAX
        }

        whenever(confirmationTokenService.getLastConfirmationToken(user.id!!)) doReturn confirmationTokenDTO

        assertThrows<UserRegistrationException>(UserRegistrationError.NEED_TO_CONFIRM_ACCOUNT.name) {
            authService.registerUser(user.firstName!!, user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `send new token for account with expired last token should throw an exception`() {
        user.confirmed = false

        val confirmationTokenDTO = ConfirmationTokenDTO().apply {
            this.expirationDate = LocalDateTime.MIN
        }

        whenever(confirmationTokenService.getLastConfirmationToken(user.id!!)) doReturn confirmationTokenDTO

        assertThrows<UserRegistrationException>(UserRegistrationError.NEW_TOKEN_WAS_SENT.name) {
            authService.registerUser(user.firstName!!, user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `create new user and send confirmation token should be ok`() {
        val confirmationToken = "token"
        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(userService.saveUser(any())) doReturn user.id!!
        whenever(confirmationTokenService.createAndSaveConfirmationToken(user.id!!)) doReturn confirmationToken

        authService.registerUser(user.firstName!!, user.username!!, user.password!!, user.roles!!)

        verify(messageSender).sendConfirmationToken(
            user.firstName!!,
            user.username!!,
            confirmationToken,
            confirmationProperties.minutes,
            confirmationProperties.serverUrl
        )
    }

    @Test
    fun `registration with invalid email should throw an exception`() {
        val confirmationToken = "token"
        user.username = "username"
        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(userService.saveUser(any())) doReturn user.id!!
        whenever(confirmationTokenService.createAndSaveConfirmationToken(user.id!!)) doReturn confirmationToken

        assertThrows<UserRegistrationException>(UserRegistrationError.INVALID_EMAIL.name) {
            authService.registerUser(user.firstName!!, user.username!!, user.password!!, user.roles!!)
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