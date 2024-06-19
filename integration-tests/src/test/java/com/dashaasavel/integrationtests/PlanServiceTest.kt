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

class PlanServiceTest : BaseServiceTest() {
    @Autowired
    private lateinit var planService: PlanServiceGrpc.PlanServiceBlockingStub

    @Test
    fun `create and save plan`() {
        val planInfo = PlanInfo.newBuilder().apply {
            this.userId = 2
            this.type = CompetitionRunType.MARATHON
            this.date = DateUtils.convertToGrpcDate(LocalDate.of(2024, 11, 17))
            this.addAllDaysOfWeek(listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY))
            this.longRunDistance = 6
        }.build()
        val request = CreatePlan.Request.newBuilder().setPlanInfo(planInfo).build()
        val response = planService.createPlan(request)

        assertNotEquals(0, response.trainingsList.size)

        val planIdentifier = PlanIdentifier.newBuilder().apply {
            this.userId = 2
            this.type = CompetitionRunType.MARATHON
        }
        val savePlanRequest = Runservice.SavePlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        planService.savePlan(savePlanRequest)

        val getPlanRequest = Runservice.GetPlan.Request.newBuilder().setPlanIdentifier(planIdentifier).build()
        val getPlanResponse = planService.getPlan(getPlanRequest)

        assertEquals(planInfo, getPlanResponse.plan.planInfo)
    }

    @Test
    fun getPlan() {
        val build = Runservice.GetPlan.Request.newBuilder().setPlanId("6672b4732b3c587c565bc959").build()
        val plan = planService.getPlan(build)
        println(plan)
    }
}

