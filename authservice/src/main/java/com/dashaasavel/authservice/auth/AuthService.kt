package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.AccessTokenError
import com.dashaasavel.authservice.AuthError
import com.dashaasavel.authservice.exceptions.AuthServiceException
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.authservice.tokens.AuthTokens
import com.dashaasavel.authservice.tokens.TokensService
import org.springframework.security.crypto.password.PasswordEncoder

class AuthService(
    private val userService: UserServiceFacade,
    private val encoder: PasswordEncoder,
    private val tokensService: TokensService
) {
    fun refreshAccessToken(refreshToken: String): String {
        val token = tokensService.findByToken(refreshToken)
                ?: throw AuthServiceException(AccessTokenError.REFRESH_TOKEN_NOT_FOUND)
        if (token.isExpired()) {
            throw AuthServiceException(AccessTokenError.REFRESH_TOKEN_EXPIRED)
        }
        return tokensService.createAccessToken(token.userId!!, token.username)
    }

    fun authUser(username: String, password: String): AuthTokens {
        val user = userService.getUser(username) ?: throw AuthServiceException(AuthError.USER_DOES_NOT_EXIST)
        if (encoder.matches(password, user.password)) {
            val tokenPair = tokensService.createTokenPair(user.id!!, username)
            return AuthTokens(tokenPair.accessToken, tokenPair.refreshToken)
        } else throw AuthServiceException(AuthError.INCORRECT_PASSWORD)
    }
}