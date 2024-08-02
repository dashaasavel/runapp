package com.dashaasavel.mailservice.message

import com.google.protobuf.Message
import jakarta.mail.internet.MimeMessage
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class WelcomeMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
) : AbstractMessageCreator(mailProperties, javaMailSender) {
    override fun createMessage(message: Message): MimeMessage {
        val welcomeMessage = message as WelcomeMessage
        val userInfo = welcomeMessage.userInfo
        return createWelcomeMessage(userInfo.username, userInfo.firstName)
    }

    private fun createWelcomeMessage(username: String, firstName: String): MimeMessage {
        val subject = "Welcome email"
        val body = "Helleeew $firstName. Here will be smth interesting"
        return createMessage(username, subject, body)
    }
}