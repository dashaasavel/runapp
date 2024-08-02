package com.dashaasavel.mailservice.rabbit

import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import com.dashaasavel.runapplib.logger

class MailMessageListener : MessageListener {
    private val logger = logger()
    override fun onMessage(message: Message) {
        val consumerQueue = message.messageProperties.consumerQueue
        logger.info("Message from queue={} was received", consumerQueue)
    }
}