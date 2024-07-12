package com.dashaasavel.runservice.mongodb

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
open class MongoDBConfig(
    private val mongoTemplate: MongoTemplate
) {
    @Bean
    open fun mongoCollectionInitializer() = MongoCollectionInitializer(mongoTemplate)
}