package com.dashaasavel.metricaggregator

import org.springframework.kafka.annotation.KafkaListener


class KafkaConsumer {
    @KafkaListener(topics = ["test"], groupId = "foo")
    fun listenGroupFoo(message: String) {
        println("Received Message in group foo: $message")
    }
}