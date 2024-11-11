package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.UserRegistrationException
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.authservice.tokens.access.AccessTokenService
import com.dashaasavel.authservice.tokens.AuthTokens
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenService
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.crypto.password.PasswordEncoder

class AuthService(
    private val userService: UserServiceFacade,
    private val encoder: PasswordEncoder,
    private val accessTokenService: AccessTokenService,
    private val refreshTokenService: RefreshTokenService
) {
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

    fun registerUser(firstName: String, username: String, password: String): Int {
        if (userService.doesUserExists(username)) {
            throw UserRegistrationException(
                UserRegistrationError.USER_EXISTS
            )

        }
        if (EmailValidator.getInstance(true).isValid(username)) {
            val encodedPassword = encoder.encode(password)
            return userService.saveUser(firstName, username, encodedPassword)
        } else {
            throw UserRegistrationException(UserRegistrationError.INVALID_EMAIL)
        }
    }
}