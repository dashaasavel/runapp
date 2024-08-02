package com.dashaasavel.mailservice.message

import com.google.protobuf.Message
import jakarta.mail.internet.MimeMessage
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class ConfirmationTokenMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
) : AbstractMessageCreator(mailProperties, javaMailSender) {
    override fun createMessage(message: Message): MimeMessage {
        val confirmationMessage = message as ConfirmationMessage
        val userInfo = confirmationMessage.userInfo
        val token = confirmationMessage.token
        val serverUrl = confirmationMessage.url
        val minutes = confirmationMessage.minutes
        return createMessage(userInfo.username, userInfo.firstName, serverUrl, token, minutes)
    }

    private fun createMessage(
        username: String, firstName: String, serverUrl: String,
        userToken: String, minutes: Int
    ): MimeMessage {
        val subject = "Confirmation email"
        val url = serverUrl + userToken
        val body = "Hello $firstName!. Click on the link to activate your account:\n$url. Link is valid $minutes"
        return createMessage(username, subject, body)
    }
}