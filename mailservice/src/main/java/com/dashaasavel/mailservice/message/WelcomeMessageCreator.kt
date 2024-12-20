package com.dashaasavel.mailservice.message

import com.dashaasavel.grpcmessages.utils.RabbitMQQueues
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender

class WelcomeMessageCreator(
    mailProperties: MailProperties,
    javaMailSender: JavaMailSender
) : AbstractMessageCreator(mailProperties, javaMailSender) {
    private val messageParser = WelcomeMessage.parser()

    // имя пользователя, название сервиса, название сервиса
    private val bodyPattern = "Привет, %s!\n" +
            "\n" +
            "Добро пожаловать в %s! Мы рады, что ты присоединился к нашему сообществу.\n" +
            "\n" +
//            "Теперь ты можешь пользоваться всеми функциями [Название сервиса]:\n" +
//            "\n" +
//            "* [Список основных функций]\n" +
//            "\n" +
            "Помни, что ты всегда можешь связаться с нами, если у тебя возникнут вопросы.\n" +
            "\n" +
            "С наилучшими пожеланиями,\n" +
            "\n" +
            "Команда %s\n"

    override fun getSubject(): String = "Добро пожаловать в $appName!\n"

    override fun getBody(message: ByteArray): String {
        val welcomeMessage = messageParser.parseFrom(message)
        return bodyPattern.format(welcomeMessage.userInfo.firstName, appName, appName)
    }

    override fun getUsername(message: ByteArray): String {
        val welcomeMessage = messageParser.parseFrom(message)
        return welcomeMessage.userInfo.username
    }

    override fun getQueueName(): String = RabbitMQQueues.USER_CREATED_MAIL_SERVICE.queueName
}