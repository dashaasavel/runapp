package com.dashaasavel.authservice.tokens.access

class AccessTokenProperties {
    var signingKey: String = ""
    var expirationTimeInMillis: Long = 3_600_000 // 60 минут
}