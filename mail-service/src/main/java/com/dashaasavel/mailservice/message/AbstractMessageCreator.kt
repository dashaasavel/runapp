package com.dashaasavel.mailservice.message

import jakarta.mail.internet.MimeMessage
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

abstract class AbstractMessageCreator(
    private val mailProperties: MailProperties,
    private val javaMailSender: JavaMailSender
) {
    protected val appName = "RunApp"
    abstract fun getSubject(): String

    abstract fun getBody(message: ByteArray): String

    abstract fun getUsername(message: ByteArray): String

    abstract fun getQueueName(): String

    fun createMimeMessage(message: ByteArray): MimeMessage {
        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setTo(getUsername(message))
        helper.setSubject(getSubject())
        helper.setText(getBody(message))
        helper.setFrom(mailProperties.username)
        return mimeMessage
    }
}
