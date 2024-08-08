package com.dashaasavel.userservice.auth.token.refresh

import java.sql.Timestamp
import java.time.Instant

class RefreshToken {
    var id: Int? = null
    var userId: Int? = null
    lateinit var username: String
    lateinit var token: String
    lateinit var expDate: Timestamp

    fun isExpired(): Boolean {
        return this.expDate.before(Timestamp.from(Instant.now()))
    }
}