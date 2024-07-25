package com.dashaasavel.runservice.training

import com.dashaasavel.runservice.mongodb.Collections
import org.bson.types.ObjectId
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

    fun deleteById(trainingsId: String) {
        mongoTemplate.remove(
            Query(Criteria.where("_id").`is`(ObjectId(trainingsId))),
            Collections.TRAININGS.collectionName
        )
    }

    fun deleteByIds(trainingsIds: List<String>) {
        val objectIds = trainingsIds.map { ObjectId(it) }
        mongoTemplate.remove(Query(Criteria.where("_id").all(objectIds)), Collections.TRAININGS.collectionName)
    }
}