package com.dashaasavel.userservice.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.runapplib.logger
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class KafkaSender(
    private val kafkaProducer: KafkaProducer<String, GrpcMetric>
): com.dashaasavel.runapplib.KafkaSender {
    private val logger = logger()

    private val topicName = KafkaTopicNames.GRPC_METRICS.topicName
    override fun send(grpcMetric: GrpcMetric) {
        val producerRecord = ProducerRecord(topicName, "userservice", grpcMetric)
        val metadata = kafkaProducer.send(producerRecord).get()
        logger.info("Kafka message was sent: offset={}, partition={}", metadata.offset(), metadata.partition())
    }
}