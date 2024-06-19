package com.dashaasavel.userservice.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
open class KafkaTopicConfig(
    private val kafkaProducerProperties: KafkaProducerProperties
) {
//    @Bean
//    open fun kafkaAdmin(): KafkaAdmin {
//        val configs: MutableMap<String, Any?> = HashMap()
//        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProducerProperties.bootstrapAddress
//        return KafkaAdmin(configs)
//    }
//
//    @Bean
//    open fun topic1(): NewTopic {
//        return NewTopic("baeldung", 1, 1.toShort())
//    }
}