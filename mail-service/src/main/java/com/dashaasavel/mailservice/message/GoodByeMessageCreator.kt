package com.dashaasavel.mailservice.message

import com.google.protobuf.Message
import jakarta.mail.internet.MimeMessage
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class GoodByeMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
): AbstractMessageCreator(mailProperties, javaMailSender) {
    override fun createMessage(message: Message): MimeMessage {
        val goodByeMessage = message as GoodByeMessage
        val userInfo = goodByeMessage.userInfo
        return createWelcomeMessage(userInfo.username, userInfo.firstName)
    }

    private fun createWelcomeMessage(username: String, firstName: String): MimeMessage {
        val subject = "Goodbye message"
        val body = "Goooooooodbye $firstName"
        return createMessage(username, subject, body)
    }
}