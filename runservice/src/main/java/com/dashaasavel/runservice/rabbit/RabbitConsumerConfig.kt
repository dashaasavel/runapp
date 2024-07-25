package com.dashaasavel.runservice.rabbit

import com.dashaasavel.runservice.plan.PlanService
import com.dashaasavel.userserviceapi.utils.RabbitMQQueues
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
    private val planService: PlanService
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
        container.messageListener = deleteUserMessageListener()
        container.connectionFactory = connectionFactory()
        container.addQueueNames(RabbitMQQueues.USER_DELETION.queueName)
        return container
    }

    @Bean
    fun deleteUserMessageListener() = DeleteUserMessageListener(planService)
}