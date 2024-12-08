package com.dashaasavel.authservice.registration

import com.dashaasavel.authservice.exceptions.AuthServiceException
import com.dashaasavel.authservice.RegistrationError
import com.dashaasavel.authservice.api.UserServiceFacade
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.crypto.password.PasswordEncoder

class RegistrationService(
    private val userService: UserServiceFacade,
    private val encoder: PasswordEncoder,
) {
    fun registerUser(firstName: String, username: String, password: String): Int {
        if (EmailValidator.getInstance(true).isValid(username)) {
            val encodedPassword = encoder.encode(password)
            try {
                return userService.saveUser(firstName, username, encodedPassword)
            } catch (e: StatusRuntimeException) {
                if (e.status.equals(Status.ALREADY_EXISTS)) {
                    throw AuthServiceException(RegistrationError.USER_EXISTS)
                }
                throw AuthServiceException(RegistrationError.INTERNAL)

            }
        } else {
            throw AuthServiceException(RegistrationError.INVALID_EMAIL)
        }
    }
}