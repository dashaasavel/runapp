package com.dashaasavel.runservice

import com.dashaasavel.runservice.api.Runservice
import com.dashaasavel.runservice.api.Runservice.SpeedRunningInfo
import com.dashaasavel.runservice.plan.PlanInfo
import com.dashaasavel.runservice.plan.training.CompetitionRunType
import com.dashaasavel.userserviceapi.utils.DateUtils

fun Runservice.CompetitionRunType.toLocalEnum() = CompetitionRunType.valueOf(this.name)
fun CompetitionRunType.toGrpcEnum() = Runservice.CompetitionRunType.valueOf(this.name)

fun TrainingType.toGrpcEnum() = Runservice.TrainingType.valueOf(this.name)

fun Training.toGrpc(): Runservice.Training = Runservice.Training.newBuilder().also {
    it.trainingNumber = this.trainingNumber
    it.weekNumber = this.weekNumber
    it.type = this.type.toGrpcEnum()
    this.distance?.let { d -> it.distance = d }
    this.speedRunningInfo?.let { info ->
        it.speedRunningInfo = SpeedRunningInfo.newBuilder().setRepetition(info.first).setDistanceInMeters(info.second).build()
    }
    it.date = Runservice.Date.newBuilder().setYear(this.date.year).setMonth(this.date.monthValue).setDay(this.date.dayOfMonth).build()
}.build()

fun PlanInfo.toGrpc(): Runservice.PlanInfo = Runservice.PlanInfo.newBuilder().also {
    it.userId = this.userId
    it.type = this.competitionRunType.toGrpcEnum()
    it.date = DateUtils.convertToGrpcDate(this.competitionDate)
    it.addAllDaysOfWeek(this.daysOfWeek.map { day -> DateUtils.convertToGrpcDayOfWeek(day) })
    it.longRunDistance = this.longRunDistance
}.build()