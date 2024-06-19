package com.dashaasavel.userservice.kafka

import org.springframework.kafka.core.KafkaTemplate

class KafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendMessage(msg: String) {
        kafkaTemplate.send("test", msg);
    }
}