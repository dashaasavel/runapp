package com.dashaasavel.runservice.training

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

const val TRAININGS_COLLECTION_NAME = "trainings"

@Document(collection = TRAININGS_COLLECTION_NAME) // TODO убрать аннотации, сделать более низкоуровнево
class Trainings(
    @Id
    var id: String? = null,
    var trainings: List<Training>? = null
)