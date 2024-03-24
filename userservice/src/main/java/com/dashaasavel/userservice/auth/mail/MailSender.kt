package com.dashaasavel.userservice.auth.mail

interface MailSender {
    fun sendToConfirm(toEmail: String, userToken: String)

    fun sendWhenConfirmed(toEmail: String)
}