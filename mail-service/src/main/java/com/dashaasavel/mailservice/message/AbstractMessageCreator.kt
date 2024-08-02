package com.dashaasavel.mailservice.message

import com.google.protobuf.Message
import jakarta.mail.internet.MimeMessage
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

abstract class AbstractMessageCreator(
    private val mailProperties: MailProperties,
    private val javaMailSender: JavaMailSender
) {
    abstract fun createMessage(message: Message): MimeMessage

    protected fun createMessage(username: String, subject: String, body: String): MimeMessage {
        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setTo(username)
        helper.setSubject(subject)
        helper.setText(body)

        helper.setFrom(mailProperties.username)
        return mimeMessage
    }
}
