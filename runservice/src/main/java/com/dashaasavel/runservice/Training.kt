package com.dashaasavel.runservice

import com.dashaasavel.runservice.plan.training.CompetitionRunType
import com.dashaasavel.runservice.plan.training.SpeedRunningHelper
import java.time.LocalDate

class Training private constructor(
    val trainingNumber: Int,
    val weekNumber: Int,
    val type: TrainingType,
    val distance: Int? = null,
    val speedRunningInfo: Pair<Int, Int>? = null,
    val date: LocalDate,
) {
    companion object {
        fun longDistanceRunning(distance: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate): Training {
            return Training(trainingNumber, weekNumber, TrainingType.LONG_DISTANCE, distance, date = date)
        }

        fun regularRunning(distance: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate): Training {
            return Training(trainingNumber, weekNumber, TrainingType.REGULAR_RUN, distance, date = date)
        }

        fun gymTraining(
            trainingNumber: Int, weekNumber: Int, date: LocalDate,
        ): Training {
            return Training(trainingNumber, weekNumber, TrainingType.GYM, date = date)
        }

        fun speedRunning(
            level: Int, trainingNumber: Int, weekNumber: Int, date: LocalDate,
        ): Training {
            val speedTrainingInfo = SpeedRunningHelper.getSpeedTraining(level)
            val distance = speedTrainingInfo.first * speedTrainingInfo.second
            return Training(trainingNumber, weekNumber, TrainingType.SPEED_RUNNING, distance, speedTrainingInfo, date)
        }

        fun finalRunning(
            trainingNumber: Int, weekNumber: Int, date: LocalDate, competitionRunType: CompetitionRunType
        ): Training {
            return Training(
                trainingNumber, weekNumber, TrainingType.COMPETITION, competitionRunType.distance, null, date
            )
        }
    }

    override fun toString(): String {
        return "Training(trainingNumber=$trainingNumber, weekNumber=$weekNumber, type=$type, distance=$distance, speedRunningInfo=$speedRunningInfo, date=$date)"
    }
}

enum class TrainingType {
    LONG_DISTANCE, REGULAR_RUN, GYM, SPEED_RUNNING, COMPETITION
}