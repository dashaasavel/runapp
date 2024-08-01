package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runapplib.logger
import com.dashaasavel.runservice.plan.type.MarathonPlanFactory
import com.dashaasavel.runservice.plan.type.PlanAbstractFactory
import com.dashaasavel.runservice.training.Ratio
import com.dashaasavel.runservice.training.Training
import com.dashaasavel.runservice.training.Trainings
import com.dashaasavel.runservice.training.TrainingsDAO
import com.dashaasavel.runservice.utils.DateUtils
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import org.springframework.transaction.support.TransactionTemplate
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

class PlanService(
    private val trainingsDAO: TrainingsDAO,
    private val planInfoDAO: PlanInfoDAO,
    private val transactionTemplate: TransactionTemplate
) {
    private val logger = logger()

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
            else -> throw CreatingPlanException(CreatingPlanError.COMPETITION_TYPE_NOT_SUPPORTED_NOW)
        }
        val trainings = when (daysOfWeek.size) {
            3 -> factory.createForThreeTimesAWeek(weeks, planInfo, ratio)
            4 -> factory.createForFourTimesAWeek(weeks, planInfo, ratio)
            5 -> factory.createForFiveTimesAWeek(weeks, planInfo, ratio)
            else -> throw CreatingPlanException(CreatingPlanError.NOT_SUPPORTED_COUNT_TIMES_A_WEEK)
        }
        plans[userId] = Plan(planInfo, trainings)

        return trainings
    }

    fun savePlan(userId: Int, type: CompetitionRunType) {
        val plan = plans[userId] ?: error("User doesn't have a plan")
        transactionTemplate.executeWithoutResult {
            val savedTrainingsIds = trainingsDAO.save(Trainings(_id = null, plan.trainings))
            plan.info.trainingsId = savedTrainingsIds
            planInfoDAO.insertPlan(plan.info)
            plans.remove(userId)
        }
    }

    fun getPlanFromRepo(userId: Int, competitionRunType: CompetitionRunType): Plan? {
        val planInfo = planInfoDAO.getPlanInfo(userId, competitionRunType) ?: return null
        val trainings = trainingsDAO.findById(planInfo.trainingsId!!)!!.trainings

        return Plan(planInfo, trainings)
    }

    fun deletePlan(userId: Int, type: CompetitionRunType) {
        transactionTemplate.executeWithoutResult {
            val trainingsId = planInfoDAO.deletePlan(userId, type) ?: kotlin.run {
                logger.info("Plan with identifier(userId={}, type={}) was not found in database", userId, type.name)
                return@executeWithoutResult
            }
            trainingsDAO.deleteById(trainingsId)
        }
    }

    fun deleteAllPlans(userId: Int): List<String> {
        return transactionTemplate.execute {
            val ids = planInfoDAO.deleteAllPlans(userId)
            trainingsDAO.deleteByIds(ids)
            ids
        }!!
    }

    private fun checkIfPlanExists(userId: Int, competitionRunType: CompetitionRunType) {
        if (planInfoDAO.isPlanExists(userId, competitionRunType)) {
            throw CreatingPlanException(CreatingPlanError.PLAN_ALREADY_EXISTS)
        }
    }
}