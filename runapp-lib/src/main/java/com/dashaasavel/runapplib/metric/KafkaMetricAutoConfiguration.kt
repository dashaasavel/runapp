package com.dashaasavel.runapplib.metric

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.runapplib.grpc.interceptor.MetricGrpcServerInterceptor
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConditionalOnProperty("metric-aggregator.kafka-sending.enabled", havingValue = "true")
class KafkaMetricAutoConfiguration {
    @Value("\${spring.application.name}")
    var applicationName: String = ""

    @Bean
    @ConfigurationProperties("kafka.metric.producer")
    fun kafkaMapProperties() = HashMap<String, String>()

    @Bean
    fun kafkaMetricProducerProperties() = Properties().apply {
        this.putAll(kafkaMapProperties())
    }

    @Bean
    fun kafkaMetricProducer(): KafkaProducer<String, GrpcMetric> {
        return KafkaProducer(kafkaMetricProducerProperties())
    }

    @Bean
    fun kafkaMetricSender() = KafkaMetricSender(kafkaMetricProducer(), applicationName)

    @Bean
    fun metricGrpcServerInterceptor() = MetricGrpcServerInterceptor(kafkaMetricSender())
}