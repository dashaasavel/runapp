package com.dashaasavel.userservice.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class KafkaConfig {
    @Bean
    @ConfigurationProperties("spring.kafka")
    open fun kafkaProperties() = KafkaProducerProperties()
}