package com.dashaasavel.integrationtests.facades

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.runservice.api.Runservice
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import com.dashaasavel.userserviceapi.utils.PlanServiceMessageWrappers
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PlanServiceFacade(
    private val planService: PlanServiceGrpc.PlanServiceBlockingStub
) {
    fun createAndSaveMarathonPlan(userId: Int): Runservice.Plan {
        val planInfo = createMarathonPlanInfo(userId)
        createPlan(planInfo)

        val planIdentifier = createMarathonPlanIdentifier(userId)
        savePlan(planIdentifier)
        val plan = getPlan(planIdentifier)

        assertFalse {
            plan.isNull()
        }
        assertEquals(userId, plan.planInfo.identifier.userId)
        return plan
    }

    fun createMarathonPlanInfo(userId: Int): Runservice.PlanInfo {
        return createPlanInfo(userId, CompetitionRunType.MARATHON)
    }

    fun createPlanInfo(userId: Int, competitionRunType: CompetitionRunType): Runservice.PlanInfo {
        val marathonDate = LocalDate.now().plusWeeks(17).with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        return PlanServiceMessageWrappers.planInfo(
            userId, competitionRunType, marathonDate,
            listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY), 6
        )
    }

    fun createMarathonPlanIdentifier(
        userId: Int,
    ): Runservice.PlanIdentifier {
        return PlanServiceMessageWrappers.planIdentifier(userId, Runservice.CompetitionRunType.MARATHON)
    }

    fun createPlan(planInfo: Runservice.PlanInfo): Runservice.CreatePlan.Response {
        val request = Runservice.CreatePlan.Request.newBuilder().setPlanInfo(planInfo).build()
        return planService.createPlan(request)
    }

    fun savePlan(planIdentifier: Runservice.PlanIdentifier) {
        val request = Runservice.SavePlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        planService.savePlan(request)
    }

    fun getPlan(planIdentifier: Runservice.PlanIdentifier): Runservice.Plan {
        val getPlanRequest = Runservice.GetPlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        return planService.getPlan(getPlanRequest).plan
    }

    fun deletePlan(planIdentifier: Runservice.PlanIdentifier) {
        val request = Runservice.DeletePlan.Request.newBuilder().apply {
            this.planIdentifier = planIdentifier
        }.build()
        planService.deletePlan(request)
    }
}