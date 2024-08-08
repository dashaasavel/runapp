package com.dashaasavel.userservice.auth.token.access

class AccessTokenProperties {
    var signingKey: String = ""
    var expirationTimeInMillis: Long = 3_600_000 // 60 минут
}