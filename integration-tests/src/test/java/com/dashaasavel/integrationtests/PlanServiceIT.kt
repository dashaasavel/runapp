package com.dashaasavel.integrationtests

import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.runservice.api.Runservice
import com.dashaasavel.runservice.api.Runservice.*
import com.dashaasavel.userserviceapi.utils.DateUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class PlanServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var planService: PlanServiceGrpc.PlanServiceBlockingStub

    @Test
    fun `create and save plan`() {
        val planInfo = PlanInfo.newBuilder().apply {
            this.userId = 4
            this.type = CompetitionRunType.MARATHON
            this.date = DateUtils.convertToGrpcDate(LocalDate.of(2024, 11, 17))
            this.addAllDaysOfWeek(listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY))
            this.longRunDistance = 6
        }.build()
        val request = CreatePlan.Request.newBuilder().setPlanInfo(planInfo).build()
        val response = planService.createPlan(request)

        assertNotEquals(0, response.trainingsList.size)
//
        val planIdentifier = PlanIdentifier.newBuilder().apply {
            this.userId = 4
            this.type = CompetitionRunType.MARATHON
        }
        val savePlanRequest = SavePlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        planService.savePlan(savePlanRequest)
//
//        val getPlanRequest = GetPlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
//        val getPlanResponse = planService.getPlan(getPlanRequest)
//
//        assertEquals(planInfo, getPlanResponse.plan.planInfo)
    }

    @Test
    fun delete() {
        val request = DeletePlan.Request.newBuilder().apply {
            this.planIdentifier = PlanIdentifier.newBuilder().setUserId(4).setType(CompetitionRunType.MARATHON).build()
        }.build()

        val response = planService.deletePlan(request)
        println(response)
    }
}

