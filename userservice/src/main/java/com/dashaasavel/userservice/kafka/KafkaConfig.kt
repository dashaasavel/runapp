package com.dashaasavel.userservice.kafka

import com.dashaasavel.metric.api.GrpcMetric
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Properties

@Configuration
class KafkaConfig {
    @Bean
    @ConfigurationProperties("kafka.producer")
    fun kafkaMapProperties() = HashMap<String, String>()

    @Bean
    fun kafkaProducerProperties() = Properties().apply {
        this.putAll(kafkaMapProperties())
    }

    @Bean
    fun kafkaProducer(): KafkaProducer<String, GrpcMetric> {
        return KafkaProducer(kafkaProducerProperties())
    }

    @Bean
    fun kafkaSender() = KafkaSender(kafkaProducer())
}