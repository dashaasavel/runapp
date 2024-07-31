package com.dashaasavel.userservice.auth

class JwtProperties {
    var signingKey: String = ""
    var expirationTimeMillis: Long = 3_600_000 // 60 минут
}