package com.dashaasavel.userservice.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.sql.Date
import javax.crypto.SecretKey

class JwtManager(
    private val jwtProperties: JwtProperties
) {
    fun createJwtToken(userId: Int, username: String): String {
        val secretKey = createSignInKey()
        return Jwts.builder()
            .subject(username)
            .claim("userId", userId)
            .signWith(secretKey)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expirationTimeMillis))
            .compact()
    }

    private fun createSignInKey(): SecretKey {
        return Keys.hmacShaKeyFor(jwtProperties.signingKey.toByteArray(StandardCharsets.UTF_8))
    }
}