package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import com.dashaasavel.userservice.auth.confirmation.ConfirmationTokenService
import com.dashaasavel.userservice.auth.token.access.AccessTokenService
import com.dashaasavel.userservice.auth.token.AuthTokens
import com.dashaasavel.userservice.auth.token.refresh.RefreshTokenService
import com.dashaasavel.userservice.rabbit.RegistrationMessageSender
import com.dashaasavel.userservice.role.Roles
import com.dashaasavel.userservice.user.User
import com.dashaasavel.userservice.user.UserService
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

class AuthService(
    private val userService: UserService,
    private val confirmationTokenService: ConfirmationTokenService,
    private val confirmationProperties: ConfirmationProperties,
    private val mailMessageSender: RegistrationMessageSender,
    private val encoder: PasswordEncoder,
    private val accessTokenService: AccessTokenService,
    private val refreshTokenService: RefreshTokenService
) {
    private val isAccountConfirmedByDefault = true

    fun refreshAccessToken(refreshToken: String): String {
        val token =
            refreshTokenService.findByToken(refreshToken) ?: throw UserAuthException(AuthError.REFRESH_TOKEN_NOT_FOUND)
        if (token.isExpired()) {
            throw UserAuthException(AuthError.REFRESH_TOKEN_EXPIRED)
        }
        return accessTokenService.createAccessToken(token.userId!!, token.username)
    }

    fun authUser(username: String, password: String): AuthTokens {
        val user = userService.getUser(username) ?: throw UserAuthException(AuthError.USER_DOES_NOT_EXIST)
        if (encoder.matches(password, user.password)) {
            val accessToken = accessTokenService.createAccessToken(user.id!!, username)
            val refreshToken = refreshTokenService.createRefreshToken(user.id!!, username)
            return AuthTokens(accessToken, refreshToken)
        } else throw UserAuthException(AuthError.INCORRECT_PASSWORD)
    }

    fun registerUser(firstName: String, username: String, password: String, roles: List<Roles>): Int {
        if (userService.isUserExists(username)) {
            val user = userService.getUser(username)!!
            if (user.confirmed!!) {
                throw UserRegistrationException(
                    UserRegistrationError.USER_EXISTS_AND_CONFIRMED
                )
            }
            val token = confirmationTokenService.getLastConfirmationToken(user.id!!)!!
            if (token.expirationDate!!.isAfter(LocalDateTime.now())) {
                throw UserRegistrationException(
                    UserRegistrationError.NEED_TO_CONFIRM_ACCOUNT
                )
            } else {
                createAndSendConfirmationToken(firstName, username, user.id!!)
                throw UserRegistrationException(
                    UserRegistrationError.NEW_TOKEN_WAS_SENT
                )
            }

        }
        if (EmailValidator.getInstance(true).isValid(username)) {
            return createUserAndSendConfirmationToken(firstName, username, password, roles)
        } else {

            throw UserRegistrationException(UserRegistrationError.INVALID_EMAIL)
        }
    }

    private fun createUserAndSendConfirmationToken(
        firstName: String, username: String, password: String, roles: List<Roles>
    ): Int {
        val encodedPassword = encoder.encode(password)
        val user = User().apply {
            this.firstName = firstName
            this.username = username
            this.password = encodedPassword
            this.roles = roles
        }
        user.confirmed = isAccountConfirmedByDefault

        val userId = userService.saveUser(user)

//        if (!isAccountConfirmedByDefault) {
        createAndSendConfirmationToken(firstName, username, userId)
//        }
        return userId
    }

    private fun createAndSendConfirmationToken(firstName: String, username: String, userId: Int) {
        val token = confirmationTokenService.createAndSaveConfirmationToken(userId)
        mailMessageSender.sendConfirmationToken(
            firstName, username, token, confirmationProperties.minutes, confirmationProperties.serverUrl
        )
    }
}