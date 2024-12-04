package com.dashaasavel.authservice.tokens.refresh

import java.sql.Timestamp
import java.time.Instant
import java.util.*

class RefreshTokenService(
    private val dao: RefreshTokenDAO,
    private val refreshTokenProperties: RefreshTokenProperties
) {
    fun findByToken(token: String): RefreshToken? {
        return dao.findToken(token)
    }

    fun createRefreshToken(userId: Int, username: String): String {
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
}