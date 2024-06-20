package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import com.dashaasavel.runapplib.grpc.error.UserRegistrationResponseError
import com.dashaasavel.userservice.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenDTO
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.mail.MailService
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User
import com.dashaasavel.userservice.user.UserService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.assertEquals

class RegistrationServiceTest {
    private val user = User().apply {
        this.id = 1
        this.username = "user1"
        this.password = "password2"
        this.roles = listOf(Roles.USER)
        this.confirmed = false
    }

    private val userService: UserService = mock {
        on { getUserByUsername(user.username!!) } doReturn user
    }
    private val mailService: MailService = mock {
        on { sendToConfirm(any(), any()) } doAnswer {}
    }
    private val profilesHelper: ProfilesHelper = mock()
    private val confirmationTokenService: ConfirmationTokenService = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private lateinit var registrationService: RegistrationService

    @BeforeEach
    fun before() {
        registrationService =
            RegistrationService(userService, mailService, profilesHelper, confirmationTokenService, passwordEncoder)
    }

    @Test
    fun `register existing and confirmed account`() {
        user.confirmed = true

        whenever(userService.isUserExists(user.username!!)) doReturn true

        assertThrows<GrpcServerException>(UserRegistrationResponseError.USER_EXISTS_AND_CONFIRMED.name) {
            registrationService.registerUser(user.username!!, user.password!!, user.roles!!)
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

        assertThrows<GrpcServerException>(UserRegistrationResponseError.NEED_TO_CONFIRM_ACCOUNT.name) {
            registrationService.registerUser(user.username!!, user.password!!, user.roles!!)
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

        assertThrows<GrpcServerException>(UserRegistrationResponseError.NEW_TOKEN_WAS_SENT.name) {
            registrationService.registerUser(user.username!!, user.password!!, user.roles!!)
        }
    }

    @Test
    fun `create new user and send token (mail service is on)`() {
        val captor = argumentCaptor<User>()
        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(profilesHelper.isMailConfirmationEnabled()) doReturn true
        whenever(userService.saveUser(captor.capture())) doReturn user.id!!
        whenever(confirmationTokenService.createAndSaveConfirmationToken(user.id!!)) doReturn "token"

        registrationService.registerUser(user.username!!, user.password!!, user.roles!!)

        assertEquals(false, captor.firstValue.confirmed)
        verify(mailService).sendToConfirm(any(), any())
    }

    @Test
    fun `create new user with confirmed account (mail service is off)`() {
        val captor = argumentCaptor<User>()

        whenever(userService.isUserExists(user.username!!)) doReturn false
        whenever(profilesHelper.isMailConfirmationEnabled()) doReturn false
        whenever(userService.saveUser(captor.capture())) doReturn user.id!!

        registrationService.registerUser(user.username!!, user.password!!, user.roles!!)

        assertEquals(true, captor.firstValue.confirmed)
    }
}