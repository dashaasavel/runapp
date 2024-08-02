package com.dashaasavel.mailservice.core

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class MailService(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
) {
    fun sendMessage() {
        // todo
    }
}