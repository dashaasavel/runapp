package com.dashaasavel.mailservice.rabbit

import com.dashaasavel.mailservice.core.MailService
import com.dashaasavel.mailservice.message.AbstractMessageCreator
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConsumerConfig(
    private val messageCreators: List<AbstractMessageCreator>,
    private val mailService: MailService
) {
    @Bean
    @ConfigurationProperties("rabbit.consumer")
    fun rabbitProperties() = RabbitProperties()

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val cachingConnectionFactory = CachingConnectionFactory(rabbitProperties().host)
        cachingConnectionFactory.username = rabbitProperties().username
        cachingConnectionFactory.setPassword(rabbitProperties().password)
        return cachingConnectionFactory
    }

    @Bean
    fun rabbitTemplate() = RabbitTemplate(connectionFactory())

    @Bean
    fun messageListenerContainer(): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.messageListener = mailMessageListener()
        container.connectionFactory = connectionFactory()
        for (queueName in messageCreators.map { it.getQueueName() }) {
            container.addQueueNames(queueName)
        }
        return container
    }

    @Bean
    fun mailMessageListener() = MailMessageListener(messageCreators, mailService)
}