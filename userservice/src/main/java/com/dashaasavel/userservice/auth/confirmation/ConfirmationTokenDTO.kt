package com.dashaasavel.userservice.auth.confirmation

import java.time.LocalDateTime

class ConfirmationTokenDTO {
    var token: String = ""
    var userId: Int? = null
    var confirmationDate: LocalDateTime? = null
    var creationDate: LocalDateTime? = null
    var expirationDate: LocalDateTime?= null
}