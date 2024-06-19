package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.Training
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

const val TRAININGS_COLLECTION_NAME = "trainings"

@Document(collection = TRAININGS_COLLECTION_NAME)
class Trainings(
    @Id
    var id: String? = null,
    var trainings: List<Training>? = null
)