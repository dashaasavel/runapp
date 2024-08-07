package com.dashaasavel.mailservice.message

import com.dashaasavel.userserviceapi.utils.RabbitMQQueues
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class ConfirmationTokenMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
) : AbstractMessageCreator(mailProperties, javaMailSender) {
    private val messageParser = ConfirmationMessage.parser()

    // имя пользователя, имя приложения, ссылка для перехода, кол-во минут активности ссылки, имя приложения, имя приложения
    private val bodyPattern = "Привет, %s!\n" +
            "\n" +
            "Спасибо за регистрацию в приложении %s! " +
            "Чтобы завершить регистрацию и получить доступ ко всем функциям сервиса, " +
            "подтвердите свой адрес электронной почты, перейдя по ссылке: %s.\n" +
            "Ссылка активна в течение %d минут." +
            "\n" +
            "Если вы не регистрировались в %s, можете просто игнорировать это письмо.\n" +
            "\n" +
            "С уважением,\n" +
            "\n" +
            "Команда %s\n"

    override fun getSubject(): String = "Подтверждение аккаунта"

    override fun getQueueName(): String = RabbitMQQueues.USER_NEED_CONFIRMATION_MAIL_SERVICE.queueName

    override fun getBody(message: ByteArray): String {
        val confirmationMessage = messageParser.parseFrom(message)
        val userInfo = confirmationMessage.userInfo
        val token = confirmationMessage.token
        val serverUrl = confirmationMessage.url
        val minutes = confirmationMessage.minutes
        val link = serverUrl + token
        return bodyPattern.format(userInfo.firstName, appName, link, minutes, appName, appName)
    }

    override fun getUsername(message: ByteArray): String = messageParser.parseFrom(message).userInfo.username
}