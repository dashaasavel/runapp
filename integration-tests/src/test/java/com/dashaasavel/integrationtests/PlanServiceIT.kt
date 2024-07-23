package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Test
    fun `create marathon plan with non existing user`() {
        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.USER_DOES_NOT_EXIST) {
            planService.createAndSaveMarathonPlan(Random().nextInt() % 5000)
        }
    }

    @Test
    fun `create 2 marathon plans`() {
        val userId = userService.registerUser()
        planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createAndSaveMarathonPlan(userId)
        }
    }

    @Test
    @Disabled
    fun `delete marathon plan`() {
        val userId = userService.registerUser()
        var identifier = planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        planService.deletePlan(planService.createMarathonPlanIdentifier(userId))
        identifier = planService.getPlan(identifier).planInfo.identifier

        assertTrue {
            identifier.isNull()
        }
    }
}

