package com.dashaasavel.userservice.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.userserviceapi.utils.KafkaTopicNames
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class KafkaSender(
    private val kafkaProducer: KafkaProducer<String, GrpcMetric>
): com.dashaasavel.runapplib.KafkaSender {
    private val topicName = KafkaTopicNames.GRPC_METRICS.topicName
    override fun send(grpcMetric: GrpcMetric) {
        val producerRecord = ProducerRecord(topicName, "userservice", grpcMetric)
        val metadata = kafkaProducer.send(producerRecord).get()
        println("SENT: offset=${metadata.offset()}, partition=${metadata.partition()}")
    }
}