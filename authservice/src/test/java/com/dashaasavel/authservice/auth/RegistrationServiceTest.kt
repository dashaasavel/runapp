package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.RegistrationError
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.registration.RegistrationService
import com.nhaarman.mockitokotlin2.*
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class RegistrationServiceTest {
    private val user = Fixtures.user

    private val encoder: PasswordEncoder = mock()
    private val userService: UserServiceFacade = mock()
    private val registrationService = RegistrationService(userService, encoder)

    @Test
    fun `when user register with existing username then exception should be thrown`() {
        whenever(encoder.encode(any())) doReturn "encodedPassword"
        whenever(userService.saveUser(any(), any(), any())) doThrow StatusRuntimeException(Status.ALREADY_EXISTS)

        assertThrowsAuthServiceException(RegistrationError.USER_EXISTS) {
            registrationService.registerUser(user.firstName, user.username, user.password)
        }
    }

    @Test
    fun `when user register with correct credentials then registration should be success`() {
        whenever(encoder.encode(any())) doReturn "encodedPassword"
        whenever(userService.saveUser(any(), any(), any())) doReturn user.id!!

        registrationService.registerUser(user.firstName, user.username, user.password)
    }

    @Test
    fun `when user register with invalid email then exception should be thrown`() {
        user.username = "username"
        whenever(userService.saveUser(any(), any(), any())) doReturn user.id!!

        assertThrowsAuthServiceException(RegistrationError.INVALID_EMAIL) {
            registrationService.registerUser(user.firstName, user.username, user.password)
        }
        user.username = "dashasaavel@gmail.com"
    }
}