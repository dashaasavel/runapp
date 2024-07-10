package com.dashaasavel.userservice.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

class KafkaSender(
    private val kafkaProducer: KafkaProducer<String, GrpcMetric>
): com.dashaasavel.runapplib.KafkaSender {
    override fun send(grpcMetric: GrpcMetric) {
        val producerRecord = ProducerRecord(KafkaTopicNames.GRPC_METRICS.topicName, "userservice", grpcMetric)
        val metadata = kafkaProducer.send(producerRecord).get()
        println(metadata.partition())
    }
}

fun main() {
    val properties = Properties()
    properties["bootstrap.servers"] = "localhost:29092"
    properties["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    properties["value.serializer"] = "io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer"
    properties["schema.registry.url"] = "http://localhost:8081"
    val producer = KafkaProducer<String, GrpcMetric>(properties)

    val grpcMetric = GrpcMetric.newBuilder().apply {
        this.timestamp = System.currentTimeMillis()
        this.serviceAndMethodName = "TEST/testMethod"
        this.channel = "lol"
    }.build()
    val producerRecord = ProducerRecord(KafkaTopicNames.GRPC_METRICS.topicName, "userservice", grpcMetric)
    val recordMetadata = producer.send(producerRecord)
    println(recordMetadata)
}