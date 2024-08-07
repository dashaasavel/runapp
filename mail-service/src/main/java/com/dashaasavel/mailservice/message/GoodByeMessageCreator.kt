package com.dashaasavel.mailservice.message

import com.dashaasavel.userserviceapi.utils.RabbitMQQueues
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class GoodByeMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
) : AbstractMessageCreator(mailProperties, javaMailSender) {
    private val messageParser = GoodByeMessage.parser()

    // имя пользователя, название сервиса, название сервиса
    private val bodyPattern = "Привет, %s!\n" +
            "\n" +
            "Мы получили твоё решение удалить аккаунт на %s. Все твои созданные планы и иформация об аккаунте будут удалены.\n" +
            "\n" +
            "Мы всегда рады были видеть тебя среди наших пользователей и надеемся, что ты снова к нам вернешься.\n" +
            "\n" +
            "С наилучшими пожеланиями,\n" +
            "\n" +
            "Команда %s"

    override fun getSubject(): String = "До свидания!" // гы

    override fun getBody(message: ByteArray): String {
        val goodByeMessage = messageParser.parseFrom(message)
        return bodyPattern.format(goodByeMessage.userInfo.firstName, appName, appName)
    }

    override fun getUsername(message: ByteArray): String {
        val goodByeMessage = messageParser.parseFrom(message)
        return goodByeMessage.userInfo.username
    }

    override fun getQueueName(): String = RabbitMQQueues.USER_DELETED_MAIL_SERVICE.queueName
}