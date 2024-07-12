package com.dashaasavel.runservice.mongodb

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoDBConfig(
    private val mongoTemplate: MongoTemplate
) {
    @Bean
    fun mongoCollectionInitializer() = MongoCollectionInitializer(mongoTemplate)
}