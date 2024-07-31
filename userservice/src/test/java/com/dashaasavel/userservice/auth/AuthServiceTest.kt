package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDTO
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailService
import com.dashaasavel.userservice.profiles.ProfilesHelper
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User
import com.dashaasavel.userservice.user.UserService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AuthServiceTest {
    private val user = User().apply {
        this.id = 1
        this.username = "user1"
        this.password = "password2"
        this.roles = listOf(Roles.USER)
        this.confirmed = false
    }

    private val userService: UserService = mock {
        on { getUser(user.username!!) } doReturn user
    }
    private val mailService: MailService = mock {
        on { sendToConfirm(any(), any()) } doAnswer {}
    }
    private val profilesHelper: ProfilesHelper = mock()
    private val confirmationTokenService: ConfirmationTokenService = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private val jwtManager: JwtManager  = mock()
    private val authService =
        AuthService(userService, mailService, profilesHelper, confirmationTokenService, passwordEncoder, jwtManager)

    @Test
    fun `register existing and confirmed account`() {
        user.confirmed = true

        whenever(userService.isUserExists(user.username!!)) doReturn true

        assertThrows<UserRegistrationException>(UserRegistrationError.USER_EXISTS_AND_CONFIRMED.name) {
            authService.registerUser(user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `register existing and not confirmed by active token account`() {
        user.confirmed = false

        val confirmationTokenDTO = ConfirmationTokenDTO().apply {
            this.expirationDate = LocalDateTime.MAX
        }

        whenever(userService.isUserExists(user.username!!)) doReturn true
        whenever(confirmationTokenService.getLastConfirmationToken(user.id!!)) doReturn confirmationTokenDTO

        assertThrows<UserRegistrationException>(UserRegistrationError.NEED_TO_CONFIRM_ACCOUNT.name) {
            authService.registerUser(user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `send new token for account with expired last token`() {
        user.confirmed = false

        val confirmationTokenDTO = ConfirmationTokenDTO().apply {
            this.expirationDate = LocalDateTime.MIN
        }

        whenever(userService.isUserExists(user.username!!)) doReturn true
        whenever(confirmationTokenService.getLastConfirmationToken(user.id!!)) doReturn confirmationTokenDTO

        assertThrows<UserRegistrationException>(UserRegistrationError.NEW_TOKEN_WAS_SENT.name) {
            authService.registerUser(user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `create new user and send token (mail service is on)`() {
        val captor = argumentCaptor<User>()
        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(profilesHelper.isMailConfirmationEnabled()) doReturn true
        whenever(userService.saveUser(captor.capture())) doReturn user.id!!
        whenever(confirmationTokenService.createAndSaveConfirmationToken(user.id!!)) doReturn "token"

        authService.registerUser(user.username!!, user.password!!, user.roles!!)

        assertEquals(false, captor.firstValue.confirmed)
        verify(mailService).sendToConfirm(any(), any())
    }

    @Test
    fun `create new user with confirmed account (mail service is off)`() {
        val captor = argumentCaptor<User>()

        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(profilesHelper.isMailConfirmationEnabled()) doReturn false
        whenever(userService.saveUser(captor.capture())) doReturn user.id!!

        authService.registerUser(user.username!!, user.password!!, user.roles!!)

        assertEquals(true, captor.firstValue.confirmed)
    }
}