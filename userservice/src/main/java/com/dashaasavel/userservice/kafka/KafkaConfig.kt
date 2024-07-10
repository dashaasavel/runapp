package com.dashaasavel.userservice.kafka

import com.dashaasavel.metric.api.GrpcMetric
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Properties

@Configuration
open class KafkaConfig {
    @Bean
    @ConfigurationProperties("kafka.producer")
    open fun kafkaMapProperties() = HashMap<String, String>()

    @Bean
    open fun kafkaProducerProperties() = Properties().apply {
        this.putAll(kafkaMapProperties())
    }

    @Bean
    open fun kafkaProducer(): KafkaProducer<String, GrpcMetric> {
        return KafkaProducer(kafkaProducerProperties())
    }

    @Bean
    open fun kafkaSender() = KafkaSender(kafkaProducer())
}