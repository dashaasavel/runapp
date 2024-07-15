package com.dashaasavel.metricaggregator.kafka.sandbox

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

fun main() {
    val properties = Properties()
    properties["bootstrap.servers"] = "localhost:29092"
    properties["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    properties["value.serializer"] = "io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer"
    properties["schema.registry.url"] = "http://localhost:8081"
    val producer = KafkaProducer<String, GrpcMetric>(properties)
    repeat(1000) { i ->
        val grpcMetric = GrpcMetric.newBuilder().apply {
            this.timestamp = System.currentTimeMillis()
            this.serviceAndMethodName = "TEST/testMethod"
            this.channel = "lol $i"
        }.build()
        val producerRecord = ProducerRecord(KafkaTopicNames.GRPC_METRICS.topicName, "userservice 2", grpcMetric)
        val recordMetadata = producer.send(producerRecord)
        println("отправлено $i: $recordMetadata")
        Thread.sleep(100)
    }
}