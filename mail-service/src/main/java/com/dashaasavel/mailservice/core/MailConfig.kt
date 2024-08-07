package com.dashaasavel.mailservice.core

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class MailConfig(
    private val mailSender: JavaMailSender
) {
    @Bean
    fun mailService() = MailService(mailSender)
}