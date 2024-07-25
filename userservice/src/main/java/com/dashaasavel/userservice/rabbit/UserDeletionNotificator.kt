package com.dashaasavel.userservice.rabbit

import com.dashaasavel.userserviceapi.utils.RabbitMQQueues
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate

class UserDeletionNotificator(
    private val rabbitTemplate: RabbitTemplate
) {
    fun notify(userId: Int) {
        rabbitTemplate.send(RabbitMQQueues.USER_DELETION.exchange, RabbitMQQueues.USER_DELETION.routingKey, Message("$userId".encodeToByteArray()))
    }
}