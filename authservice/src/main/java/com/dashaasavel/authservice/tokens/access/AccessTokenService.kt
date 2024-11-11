package com.dashaasavel.authservice.tokens.access

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.sql.Date
import javax.crypto.SecretKey

class AccessTokenService(
    private val accessTokenProperties: AccessTokenProperties
) {
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