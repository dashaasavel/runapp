package com.dashaasavel.userservice.auth.mail

import com.dashaasavel.runapplib.logger
import com.dashaasavel.userservice.profiles.ProfilesHelper
import com.dashaasavel.userservice.auth.confirmation.ConfirmationProperties
import jakarta.mail.MessagingException
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

class MailService(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
    private val confirmationProperties: ConfirmationProperties,
    private val profilesHelper: ProfilesHelper
): MailSender {
    private val logger = logger()

    override fun sendToConfirm(toEmail: String, userToken: String) {
        if (!confirmationProperties.enabled || !profilesHelper.isMailConfirmationEnabled()) {
            return
        }
        try{
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")
            helper.setTo(toEmail)
            helper.setSubject("Confirmation email")

            val url = confirmationProperties.serverUrl+userToken
            helper.setText("Click on the link to activate your account:\n$url")

            helper.setFrom(mailProperties.username)
            mailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            throw IllegalStateException("sending message failed")
        }
    }

    override fun sendWhenConfirmed(toEmail: String) {
        if (!confirmationProperties.enabled || !profilesHelper.isMailConfirmationEnabled()) {
            return
        }
        try{
            val helloMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(helloMessage, "utf-8")
            helper.setFrom(mailProperties.username)
            helper.setTo(toEmail)
            helper.setSubject("Account confirmed")
            helper.setText("")
            mailSender.send(helloMessage)
        } catch (e: MessagingException){
            logger.error("Sending message after confirmation failed", e)
            throw IllegalStateException("sending message failed");
        }
    }
}