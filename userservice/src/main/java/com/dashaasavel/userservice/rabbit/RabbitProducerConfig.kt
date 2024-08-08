package com.dashaasavel.userservice.rabbit

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitProducerConfig {
    @Bean
    @ConfigurationProperties("rabbit.producer")
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
    fun rabbitMessageSender() = RabbitMessageSender(rabbitTemplate())
}