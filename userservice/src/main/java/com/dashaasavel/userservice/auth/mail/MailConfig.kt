package com.dashaasavel.userservice.auth.mail

import com.dashaasavel.userservice.profiles.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class MailConfig(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
    private val confirmationProperties: ConfirmationProperties,
    private val env: Environment
) {
    @Bean
    fun mailService(): MailSender = MailService(mailSender, mailProperties, confirmationProperties, profilesHelper())

    @Bean
    fun profilesHelper() = ProfilesHelper(env)
}