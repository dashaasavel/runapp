package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.Training
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.jdbc.core.JdbcTemplate

interface TrainingDAO: MongoRepository<TrainingList, String>

@Document
class TrainingList(
    @Id
    var id: String? = null,
    var trainings: List<Training>? = null
)