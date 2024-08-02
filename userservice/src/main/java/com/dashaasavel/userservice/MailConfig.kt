package com.dashaasavel.userservice

import com.dashaasavel.userservice.rabbit.RabbitMessageSender
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MailConfig(
    private val rabbitTemplate: RabbitTemplate,
) {
    @Bean
    fun rabbitMessageSender() = RabbitMessageSender(rabbitTemplate)
}