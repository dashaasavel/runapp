package com.dashaasavel.runapplib.metric

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.runapplib.logger
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class KafkaMetricSender(
    private val kafkaProducer: KafkaProducer<String, GrpcMetric>,
    private val serviceName: String
) {
    private val logger = logger()

    private val topicName = KafkaTopicNames.GRPC_METRICS.topicName
    fun send(grpcMetric: GrpcMetric) {
        val producerRecord = ProducerRecord(topicName, serviceName, grpcMetric)
        val metadata = kafkaProducer.send(producerRecord).get()
        logger.info("Message has been sent to kafka: offset={}, partition={}", metadata.offset(), metadata.partition())
    }
}