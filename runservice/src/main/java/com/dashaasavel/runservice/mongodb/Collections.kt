package com.dashaasavel.runservice.mongodb

enum class Collections(var collectionName: String) {
    TRAININGS("trainings");

    companion object {
        val values = Collections.values()
    }
}