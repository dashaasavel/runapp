package com.dashaasavel.runservice.mongodb

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.mongodb.core.MongoTemplate

class MongoCollectionInitializer(
    private val mongoTemplate: MongoTemplate
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        for (collection in Collections.values) {
            mongoTemplate.createCollection(collection.collectionName)
        }
    }
}