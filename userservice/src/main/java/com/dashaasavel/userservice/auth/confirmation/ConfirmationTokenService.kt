package com.dashaasavel.userservice.auth.confirmation

import java.time.LocalDateTime
import java.util.*

class ConfirmationTokenService(
    private val confirmationTokenDAO: ConfirmationTokenDAO,
    private val confirmationProperties: ConfirmationProperties
) {
    fun createAndSaveConfirmationToken(userId: Int): String {
        val token = UUID.randomUUID().toString()
        val confirmationTokenDTO = ConfirmationTokenDTO().apply {
            this.userId = userId
            this.token = token
            this.creationDate = LocalDateTime.now()
            this.expirationDate = creationDate!!.plusMinutes(confirmationProperties.minutes.toLong())
        }
        confirmationTokenDAO.addToken(confirmationTokenDTO)
        return token
    }

    fun confirmToken(token: String, confirmedTime: LocalDateTime) {
        confirmationTokenDAO.setConfirmed(token,confirmedTime)
    }

    fun getLastConfirmationToken(userId: Int): ConfirmationTokenDTO {
        return confirmationTokenDAO.getLastConfirmationTokenByUserId(userId)
    }

    fun checkTokenAndGetUserId(token: String): Int {
        val userId =  confirmationTokenDAO.getUserIdByToken(token)
        val lastToken = confirmationTokenDAO.getLastConfirmationTokenByUserId(userId).token
        if (lastToken != token) {
            throw IllegalStateException(" существует другой токен")
        }
        return userId
    }
}