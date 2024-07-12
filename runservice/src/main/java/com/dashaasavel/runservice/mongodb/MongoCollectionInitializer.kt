package com.dashaasavel.runservice.mongodb

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.mongodb.core.MongoTemplate

// TODO: найти норм решение для организации изменений в MongoDB
class MongoCollectionInitializer(
    private val mongoTemplate: MongoTemplate
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        for (collection in Collections.values) {
            mongoTemplate.createCollection(collection.collectionName)
        }
    }
}