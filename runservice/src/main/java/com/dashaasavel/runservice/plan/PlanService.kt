package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runservice.api.UserService
import com.dashaasavel.runservice.plan.type.MarathonPlanFactory
import com.dashaasavel.runservice.plan.type.PlanAbstractFactory
import com.dashaasavel.runservice.training.*
import com.dashaasavel.runservice.utils.DateUtils
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

class PlanService(
    private val trainingsDAO: TrainingsDAO,
    private val planInfoDAO: PlanInfoDAO,
    private val userService: UserService
) {
    private val plans = ConcurrentHashMap<Int, Plan>()
    fun createPlan(
        userId: Int, type: CompetitionRunType, competitionDate: LocalDate,
        daysOfWeek: List<DayOfWeek>, longRunDistance: Int
    ): List<Training> {
        checkIfPlanExists(userId, type)
        checkIfUserExists(userId)
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

    // TODO make transactional
    fun savePlan(userId: Int, type: CompetitionRunType) {
        checkIfUserExists(userId)
        val plan = plans[userId] ?: error("User doesn't have a plan")
        val savedTrainingsIds = trainingsDAO.save(Trainings(_id = null, plan.trainings))
        plan.info.trainingsId = savedTrainingsIds
        planInfoDAO.insertPlan(plan.info)
    }

    fun getPlanFromRepo(userId: Int, competitionRunType: CompetitionRunType): Plan? {
        val planInfo = planInfoDAO.getPlanInfo(userId, competitionRunType) ?: return null
        val trainings = trainingsDAO.findById(planInfo.trainingsId!!)!!.trainings

        return Plan(planInfo, trainings)
    }

    // TODO make transactional
    fun deletePlan(userId: Int, type: CompetitionRunType) {
        val trainingsId = planInfoDAO.deletePlan(userId, type)
        trainingsDAO.deleteById(trainingsId)
    }

    private fun checkIfPlanExists(userId: Int, competitionRunType: CompetitionRunType) {
        if (planInfoDAO.isPlanExists(userId, competitionRunType)) {
            throw CreatingPlanException(CreatingPlanError.PLAN_ALREADY_EXISTS)
        }
    }

    private fun checkIfUserExists(userId: Int) {
        if (!userService.isUserExists(userId)) {
            throw CreatingPlanException(CreatingPlanError.USER_DOES_NOT_EXIST)
        }
    }
}