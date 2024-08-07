package com.dashaasavel.mailservice.rabbit

import com.dashaasavel.mailservice.core.MailService
import com.dashaasavel.mailservice.message.AbstractMessageCreator
import com.dashaasavel.runapplib.core.logger
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class MailMessageListener(
    private val messageCreators: List<AbstractMessageCreator>,
    private val mailService: MailService
) : MessageListener {
    private val logger = logger()

    override fun onMessage(message: Message) {
        val consumerQueue = message.messageProperties.consumerQueue
        logger.info("Message from queue={} was received", consumerQueue)

        val messageCreator =
            messageCreators.find { it.getQueueName() == consumerQueue } ?: error("Unexpected queue name")
        val mimeMessage = messageCreator.createMimeMessage(message.body)
        mailService.sendMessage(mimeMessage)
    }
}