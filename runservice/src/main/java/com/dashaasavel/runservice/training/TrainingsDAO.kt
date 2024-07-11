package com.dashaasavel.runservice.training

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

open class TrainingsDAO(
    private val mongoTemplate: MongoTemplate
) {
    fun save(trainings: Trainings): String {
        val savedTrainings = mongoTemplate.insert(trainings, TRAININGS_COLLECTION_NAME)
        return savedTrainings.id!!
    }

    fun findById(id: String): Trainings? {
        return mongoTemplate.findById(id, Trainings::class.java, TRAININGS_COLLECTION_NAME)
    }

    fun deleteById(id: String) {
        mongoTemplate.remove(Query(Criteria.where("id").`is`(id)), TRAININGS_COLLECTION_NAME)
    }
}