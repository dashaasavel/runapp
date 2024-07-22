package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.runservice.api.Runservice.*
import com.dashaasavel.userserviceapi.utils.DateUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var planService: PlanServiceGrpc.PlanServiceBlockingStub

    @Test
    fun `creation and saving marathon plan`() {
        val competitionRunType = CompetitionRunType.MARATHON
        val userId = Random().nextInt()
        val planInfo = createPlanInfo(userId, competitionRunType)

        val trainingList = createPlan(planInfo).trainingsList

        assertNotEquals(0, trainingList.size)

        val planIdentifier = createPlanIdentifier(userId, competitionRunType)
        savePlan(planIdentifier)

        val savedPlan = getPlan(planIdentifier)

        assertEquals(planInfo, savedPlan.planInfo)
        assertNotEquals(0, trainingList.size)
    }

    @Test
    fun `deletion marathon plan`() {
        val competitionRunType = CompetitionRunType.MARATHON
        val userId = Random().nextInt()
        val planInfo = createPlanInfo(userId, competitionRunType)

        createPlan(planInfo)

        val planIdentifier = createPlanIdentifier(userId, competitionRunType)
        savePlan(planIdentifier)

        var savedPlan = getPlan(planIdentifier)

        assertEquals(planInfo, savedPlan.planInfo)

        deletePlan(planIdentifier)

        savedPlan = getPlan(planIdentifier)

        assertTrue {
            savedPlan.isNull()
        }
    }

    private fun createPlanInfo(userId: Int, competitionRunType: CompetitionRunType): PlanInfo {
        val marathonDate = LocalDate.now().plusWeeks(17).with(TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY))
        return PlanInfo.newBuilder().apply {
            this.userId = userId
            this.type = competitionRunType
            this.date = DateUtils.convertToGrpcDate(marathonDate)
            this.addAllDaysOfWeek(listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY))
            this.longRunDistance = 6
        }.build()
    }

    private fun createPlanIdentifier(userId: Int, competitionRunType: CompetitionRunType): PlanIdentifier {
        return PlanIdentifier.newBuilder().apply {
            this.userId = userId
            this.type = competitionRunType
        }.build()
    }

    private fun createPlan(planInfo: PlanInfo): CreatePlan.Response {
        val request = CreatePlan.Request.newBuilder().setPlanInfo(planInfo).build()
        return planService.createPlan(request)
    }

    private fun savePlan(planIdentifier: PlanIdentifier) {
        val request = SavePlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        planService.savePlan(request)
    }

    private fun getPlan(planIdentifier: PlanIdentifier): Plan {
        val getPlanRequest = GetPlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        return planService.getPlan(getPlanRequest).plan
    }

    private fun deletePlan(planIdentifier: PlanIdentifier) {
        val request = DeletePlan.Request.newBuilder().apply {
            this.planIdentifier = planIdentifier
        }.build()
        planService.deletePlan(request)
    }
}

