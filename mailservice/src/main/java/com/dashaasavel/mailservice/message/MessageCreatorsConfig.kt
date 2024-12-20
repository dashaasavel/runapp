package com.dashaasavel.mailservice.message

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class MessageCreatorsConfig(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
) {

    @Bean
    fun goodByeMessageCreator() = GoodByeMessageCreator(mailProperties, mailSender)

    @Bean
    fun welcomeMessageCreator() = WelcomeMessageCreator(mailProperties, mailSender)
}