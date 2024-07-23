package com.dashaasavel.runservice.utils

import com.dashaasavel.runservice.api.Runservice
import com.dashaasavel.runservice.plan.PlanInfo
import com.dashaasavel.runservice.training.Training
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import com.dashaasavel.userserviceapi.utils.PlanServiceMessageWrappers

fun Runservice.CompetitionRunType.toLocalEnum() = CompetitionRunType.valueOf(this.name)

fun Training.toGrpc(): Runservice.Training {
    if (this.distance == null) {
        return PlanServiceMessageWrappers.gymTraining(this.trainingNumber, this.weekNumber, this.type, this.date)
    }
    if (this.speedRunningInfo == null) {
        return PlanServiceMessageWrappers.training(
            this.trainingNumber, this.weekNumber, this.type, this.distance, this.date
        )
    }
    return PlanServiceMessageWrappers.speedTraining(
        this.trainingNumber, this.weekNumber, this.type, this.distance,
        this.date, this.speedRunningInfo.first, this.speedRunningInfo.second
    )
}

fun PlanInfo.toGrpc(): Runservice.PlanInfo {
    return PlanServiceMessageWrappers.planInfo(
        this.userId,
        this.competitionRunType,
        this.competitionDate,
        this.daysOfWeek,
        this.longRunDistance
    )
}