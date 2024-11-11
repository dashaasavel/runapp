package com.dashaasavel.userservice.rabbit

import com.dashaasavel.mailservice.message.ConfirmationMessage
import com.dashaasavel.mailservice.message.GoodByeMessage
import com.dashaasavel.mailservice.message.UserInfo
import com.dashaasavel.mailservice.message.WelcomeMessage
import com.dashaasavel.runapplib.core.logger
import com.dashaasavel.grpcmessages.utils.RabbitMQQueues
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitMessageSender(
    private val rabbitTemplate: RabbitTemplate,
): WelcomeMessageSender, UserDeletionSender {
    private val logger = logger()
    override fun sendWelcomeMessage(firstName: String, username: String) {
        val welcomeMessage = WelcomeMessage.newBuilder().apply {
            this.userInfo = UserInfo.newBuilder().setFirstName(firstName).setUsername(username).build()
        }.build()
        rabbitTemplate.send(
            RabbitMQQueues.USER_CREATED_MAIL_SERVICE.exchange,
            RabbitMQQueues.USER_CREATED_MAIL_SERVICE.routingKey,
            Message(welcomeMessage.toByteArray())
        )
        logger.info("Have sent welcome message={firstName={}, username={}} to rabbitmq", firstName, username)
    }

    override fun sendUserDeletion(firstName: String, username: String, userId: Int) {
        val goodByeMessage = GoodByeMessage.newBuilder().apply {
            this.userInfo = UserInfo.newBuilder().setFirstName(firstName).setUsername(username).build()
            this.userId = userId
        }.build()
        rabbitTemplate.send(
            RabbitMQQueues.USER_DELETED_RUN_SERVICE.exchange,
            RabbitMQQueues.USER_DELETED_RUN_SERVICE.routingKey,
            Message(goodByeMessage.toByteArray())
        )
        logger.info("Have sent user deletion token message={firstName={}, username={}} to rabbitmq", firstName, username)
    }
}

interface WelcomeMessageSender {
    fun sendWelcomeMessage(firstName: String, username: String)
}

interface UserDeletionSender {
    fun sendUserDeletion(firstName: String, username: String, userId: Int)
}