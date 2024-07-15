package com.dashaasavel.metricaggregator.kafka.sandbox

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*

fun main() {
    // начинает читать сначала
    val properties = Properties()
    properties["bootstrap.servers"] = "localhost:29092"
    properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = "org.apache.kafka.common.serialization.StringDeserializer"
    properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = "io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer"
    properties[KafkaProtobufDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = "http://localhost:8081"
    properties[KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE] = GrpcMetric::class.java.name
    properties["group.id"] = "1"
    val consumer = KafkaConsumer<String, GrpcMetric>(properties)
    consumer.subscribe(listOf(KafkaTopicNames.GRPC_METRICS.topicName))
    while (true) {
        val records = consumer.poll(Duration.ofMillis(100))
        for (record in records) {
            val key = record.key()
            val value = record.value()
            val partition = record.partition()
            println("partition: $partition")
            println("key: $key")
            println("value Metric: channel=${value.channel}, timestamp: ${value.timestamp}, ${value.serviceAndMethodName}")
        }
    }
}