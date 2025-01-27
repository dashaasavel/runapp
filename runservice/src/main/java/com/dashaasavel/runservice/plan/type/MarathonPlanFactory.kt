package com.dashaasavel.runservice.plan.type

import com.dashaasavel.runservice.training.Training
import com.dashaasavel.runservice.plan.utils.MarathonValidator
import com.dashaasavel.runservice.plan.PlanInfo
import com.dashaasavel.runservice.utils.DateUtils
import com.dashaasavel.grpcmessages.utils.CompetitionRunType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class MarathonPlanFactory : PlanAbstractFactory() {
    override fun getCompetitionRunType(): CompetitionRunType = CompetitionRunType.MARATHON

    override fun validate(planInfo: PlanInfo) {
        val weeks = DateUtils.countOfWeeks(LocalDate.now(), planInfo.competitionDate)
        MarathonValidator.validateMarathon(weeks)
    }

    override fun createForThreeTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training> {
        validate(planInfo)
        val trainings = mutableListOf<Training>()
        var startWeek = DateUtils.getNextOrSameMonday()

        val timesAWeek = planInfo.daysOfWeek
        for (week in 1 until weeks) {
            val dist: IntArray = distanceForTrainings(ratio[week])
            val dates = datesForAWeek(startWeek, timesAWeek)
            trainings += Training.regularRunning(dist[0], week * 3 - 2, week, dates[0])
            trainings += Training.regularRunning(dist[1], week * 3 - 1, week, dates[1])
            trainings += Training.longDistanceRunning(dist[2], week * 3, week, dates[2])
            startWeek = startWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        }
        val trainingNumber = (weeks - 1) * 3
        trainings += createLastWeek(trainingNumber, weeks, startWeek, planInfo.competitionDate)
        return trainings
    }

    override fun createForFourTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training> {
        validate(planInfo)
        val trainings = mutableListOf<Training>()
        var startWeek = DateUtils.getNextOrSameMonday()
        val timesAWeek = planInfo.daysOfWeek
        var level = 1
        for (week in 1 until weeks) {
            if (week > 18) {
                level = 3
            } else if (week > 13) {
                level = 2
            }
            val dist = distanceForTrainings(ratio[week])
            val dates = datesForAWeek(startWeek, timesAWeek)
            trainings += Training.regularRunning(dist[0], week * 4 - 3, week, dates[0])
            val speedRunning = Training.speedRunning(level, week * 4 - 2, week, dates[1])
            trainings += speedRunning
            trainings += Training.regularRunning(dist[1] - speedRunning.distance!!, week * 4 - 1, week, dates[2])
            trainings += Training.longDistanceRunning(dist[2], week * 4, week, dates[3])
            startWeek = startWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        }
        val trainingNumber = (weeks - 1) * 4
        trainings += createLastWeek(trainingNumber, weeks, startWeek, planInfo.competitionDate)
        return trainings
    }

    override fun createForFiveTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training> {
        validate(planInfo)
        val trainings = mutableListOf<Training>()
        var startWeek = DateUtils.getNextOrSameMonday()
        val timesAWeek = planInfo.daysOfWeek
        var level = 1
        for (week in 1 until weeks) {
            if (week > 18) {
                level = 3
            } else if (week > 13) {
                level = 2
            }
            val dist = distanceForTrainings(ratio[week])
            val dates = datesForAWeek(startWeek, timesAWeek)
            trainings.add(Training.regularRunning(dist[0], week * 5 - 4, week, dates[0]))
            val speedRunning = Training.speedRunning(level, week * 5 - 3, week, dates[1])
            trainings += Training.regularRunning(dist[1] - speedRunning.distance!!, week * 5 - 2, week, dates[2])
            trainings += Training.gymTraining(week * 5 - 1, week, dates[3])
            trainings += Training.longDistanceRunning(dist[2], week * 5, week, dates[4])
            startWeek = startWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        }
        val trainingNumber = (weeks - 1) * 5
        trainings += createLastWeek(trainingNumber, weeks, startWeek, planInfo.competitionDate)
        return trainings
    }
}