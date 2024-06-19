package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.Training
import com.dashaasavel.runservice.plan.training.CompetitionRunType
import com.dashaasavel.runservice.plan.training.Ratio
import com.dashaasavel.runservice.plan.type.MarathonPlanFactory
import com.dashaasavel.runservice.plan.type.PlanAbstractFactory
import com.dashaasavel.runservice.plan.utils.CreatingPlanError
import com.dashaasavel.runservice.utils.DateUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

open class PlanService(
    private val trainingDAO: TrainingDAO,
    private val planInfoDAO: PlanInfoDAO
) {
    private val plans = ConcurrentHashMap<Int, Plan>()
    fun createPlan(
        userId: Int,
        type: CompetitionRunType,
        competitionDate: LocalDate,
        daysOfWeek: List<DayOfWeek>,
        longRunDistance: Int
    ): List<Training> {
        val planInfo = PlanInfo(id = null, trainingListId = null, userId, type, competitionDate, daysOfWeek, longRunDistance)
        val weeks = DateUtils.countOfWeeks(LocalDate.now(), competitionDate)
        val ratio = Ratio.ratio(weeks, longRunDistance)
        val factory: PlanAbstractFactory = when (type) {
            CompetitionRunType.MARATHON -> MarathonPlanFactory()
            else -> throw UnsupportedOperationException(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW.name)
        }
        val trainings = when (daysOfWeek.size) {
            3 -> factory.createForThreeTimesAWeek(weeks, planInfo, ratio)
            4 -> factory.createForFourTimesAWeek(weeks, planInfo, ratio)
            5 -> factory.createForFiveTimesAWeek(weeks, planInfo, ratio)
            else -> throw UnsupportedOperationException(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK.name)
        }
        plans[userId] = Plan(planInfo, trainings)

        return trainings
    }

    //    @Transactional
    open fun savePlan(userId: Int, type: CompetitionRunType) {
        val plan = plans[userId] ?: error("User doesn't have a plan")
        val savedTrainingListId = trainingDAO.save(TrainingList(id = null, plan.trainings)).id!!
        plan.info.trainingListId = savedTrainingListId
        planInfoDAO.insertPlan(plan.info)
    }

    fun getPlanFromRepo(trainingListId: String): Plan {
        val planInfo = planInfoDAO.getPlanInfo(trainingListId)?: error("PlanNotFound")
        val trainingList = trainingDAO.findById(trainingListId)
            .orElseThrow { IllegalStateException("Plan $trainingListId not found") }.trainings

        return Plan(planInfo, trainingList)
    }

    fun deletePlan(userId: Int?, planId: String?, type: CompetitionRunType?) {
        if (planId != null) {
            planInfoDAO.deletePlan(planId)
        } else if (userId != null && type != null) {
            planInfoDAO.deletePlan(userId, type)
        } else throw IllegalStateException() // hype
    }
}