package com.dashaasavel.metricaggregator.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.metricaggregator.metric.MetricDAO
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Properties


@Configuration
open class KafkaConsumerConfig(
    private val metricDAO: MetricDAO
) {
    @Bean
    open fun kafkaConsumer() : KafkaConsumer<String, GrpcMetric> {
        val kafkaConsumer = KafkaConsumer<String, GrpcMetric>(kafkaConsumerProperties())
        kafkaConsumer.subscribe(listOf(KafkaTopicNames.GRPC_METRICS.topicName))
        return kafkaConsumer
    }

    @Bean
    open fun kafkaMessageProcessor() = KafkaMessageProcessor(kafkaConsumer(), metricDAO)

    @Bean
    @ConfigurationProperties("kafka.consumer")
    open fun kafkaConsumerPropertiesMap() = HashMap<String, String>()

    @Bean
    open fun kafkaConsumerProperties() = Properties().also {
        it.putAll(kafkaConsumerPropertiesMap())
    }
}

@Configuration
open class TestConfig(
    private val kafkaConsumerPropertiesMap: Map<String, String>
) {
    @PostConstruct
    fun post() {
        println(kafkaConsumerPropertiesMap)
    }
}