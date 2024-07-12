package com.dashaasavel.runservice.training

import com.dashaasavel.runservice.mongodb.Collections
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class TrainingsDAO(
    private val mongoTemplate: MongoTemplate
) {
    fun save(trainings: Trainings): String {
        val savedTrainings = mongoTemplate.insert(trainings, Collections.TRAININGS.collectionName)
        return savedTrainings._id!!
    }

    fun findById(id: String): Trainings? {
        return mongoTemplate.findById(id, Trainings::class.java, Collections.TRAININGS.collectionName)
    }

    fun deleteById(id: String) {
        mongoTemplate.remove(Query(Criteria.where("id").`is`(id)), Collections.TRAININGS.collectionName)
    }
}