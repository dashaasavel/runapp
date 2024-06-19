package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.Training
import com.dashaasavel.runservice.TrainingsDAO
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
    private val trainingsDAO: TrainingsDAO,
    private val planInfoDAO: PlanInfoDAO
) {
    private val plans = ConcurrentHashMap<Int, Plan>()
    fun createPlan(
        userId: Int, type: CompetitionRunType, competitionDate: LocalDate,
        daysOfWeek: List<DayOfWeek>, longRunDistance: Int
    ): List<Training> {
        checkIfPlanExists(userId, type)
        val planInfo =
            PlanInfo(id = null, trainingsId = null, userId, type, competitionDate, daysOfWeek, longRunDistance)
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

    // TODO make transactional
    open fun savePlan(userId: Int, type: CompetitionRunType) {
        val plan = plans[userId] ?: error("User doesn't have a plan")
        val savedTrainingsIds = trainingsDAO.save(Trainings(id = null, plan.trainings))
        plan.info.trainingsId = savedTrainingsIds
        planInfoDAO.insertPlan(plan.info)
    }

    fun getPlanFromRepo(userId: Int, competitionRunType: CompetitionRunType): Plan {
        val planInfo =
            planInfoDAO.getPlanInfo(userId, competitionRunType) ?: throw IllegalStateException("Plan not found")
        val trainings = trainingsDAO.findById(planInfo.trainingsId!!)?.trainings
            ?: throw IllegalStateException("Plan ${planInfo.trainingsId} not found")

        return Plan(planInfo, trainings)
    }

    // TODO make transactional
    fun deletePlan(userId: Int, type: CompetitionRunType) {
        val trainingsId = planInfoDAO.deletePlan(userId, type)
        trainingsDAO.deleteById(trainingsId)
    }

    private fun checkIfPlanExists(userId: Int, competitionRunType: CompetitionRunType) {
        if (planInfoDAO.isPlanExists(userId, competitionRunType)) {
            throw IllegalArgumentException(CreatingPlanError.PLAN_ALREADY_EXISTS.name)
        }
    }
}