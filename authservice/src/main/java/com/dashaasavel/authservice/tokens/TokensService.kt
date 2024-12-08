package com.dashaasavel.authservice.tokens

import com.dashaasavel.authservice.tokens.access.AccessTokenProperties
import com.dashaasavel.authservice.tokens.refresh.RefreshToken
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenDAO
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

class TokensService(
    private val dao: RefreshTokenDAO,
    private val refreshTokenProperties: RefreshTokenProperties,
    private val accessTokenProperties: AccessTokenProperties
) {
    fun createTokenPair(userId: Int, username: String): AuthTokens {
        val refreshToken = createRefreshToken(userId, username)
        val accessToken = createAccessToken(userId, username)
        return AuthTokens(accessToken, refreshToken)
    }

    private fun createRefreshToken(userId: Int, username: String): String {
        val token = UUID.randomUUID().toString()
        val refreshToken = RefreshToken().apply {
            this.userId = userId
            this.username = username
            this.token = token
            this.expDate = Timestamp.from(Instant.now().plusSeconds(refreshTokenProperties.expirationInMillis))
        }
        dao.saveToken(refreshToken)
        return token
    }

    fun findByToken(token: String): RefreshToken? {
        return dao.findToken(token)
    }

    fun createAccessToken(userId: Int, username: String): String {
        val secretKey = createSignInKey()
        return Jwts.builder()
            .subject(username)
            .claim("userId", userId)
            .signWith(secretKey)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + accessTokenProperties.expirationTimeInMillis))
            .compact()
    }

    private fun createSignInKey(): SecretKey {
        return Keys.hmacShaKeyFor(accessTokenProperties.signingKey.toByteArray(StandardCharsets.UTF_8))
    }
}