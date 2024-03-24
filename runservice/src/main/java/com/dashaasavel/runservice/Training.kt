package com.dashaasavel.runservice

import com.dashaasavel.runservice.plan.training.SpeedRunningHelper
import java.time.LocalDate

class Training private constructor(
    val trainingNumber: Int,
    val weekNumber: Int,
    val type: TrainType,
    val distance: Int?,
    val speedRunningInfo: Pair<Int, Int>?,
    val date: LocalDate,
) {
    companion object {
        fun longDistanceRunning(distance: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate): Training {
            return Training(trainingNumber, weekNumber, TrainType.LONG_DISTANCE, distance, null, date)
        }

        fun regularRunning(distance: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate): Training {
            return Training(trainingNumber, weekNumber, TrainType.LONG_DISTANCE, distance, null, date)
        }

        fun gymTraining(
            trainingNumber: Int, weekNumber: Int, date: LocalDate,
        ): Training {
            return Training(trainingNumber, weekNumber, TrainType.GYM, null, null, date)
        }

        fun speedRunning(
            level: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate,
        ): Training {
            val speedTrainingInfo = SpeedRunningHelper.getSpeedTraining(level)
            val distance = speedTrainingInfo.first * speedTrainingInfo.second
            return Training(trainingNumber, weekNumber, TrainType.SPEED_RUNNING, distance, speedTrainingInfo, date)
        }

        fun finalRunning(
            trainingNumber: Int, weekNumber: Int, date: LocalDate,
        ): Training {
            return Training(trainingNumber, weekNumber, TrainType.COMPETITION, 42, null, date)
        }
    }
}

enum class TrainType {
    LONG_DISTANCE, REGULAR_RUN, GYM, SPEED_RUNNING, COMPETITION
}