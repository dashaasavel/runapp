package com.dashaasavel.mailservice.core

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class MailConfig(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
) {
    @Bean
    fun mailService() = MailService(mailSender, mailProperties)
}