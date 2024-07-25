package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import com.dashaasavel.userserviceapi.utils.PlanServiceMessageWrappers
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Test
    fun `create marathon plan with non existing user (throw exception)`() {
        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.USER_DOES_NOT_EXIST) {
            planService.createAndSaveMarathonPlan(Random().nextInt() % 5000)
        }
    }

    @Test
    fun `create 2 marathon plans (throw exception)`() {
        val userId = userService.registerUser()
        planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createAndSaveMarathonPlan(userId)
        }
    }

    @Test
    fun `delete marathon plan (success)`() {
        val userId = userService.registerUser()
        var identifier = planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        planService.deletePlan(identifier)
        identifier = planService.getPlan(identifier).planInfo.identifier

        assertTrue {
            identifier.isNull()
        }
    }

    @Test
    fun `delete plan that does not exist (should not throw)`() {
        val userId = userService.registerUser()
        val identifier = PlanServiceMessageWrappers.planIdentifier(userId, CompetitionRunType.MARATHON)
        assertDoesNotThrow {
            planService.deletePlan(identifier)
        }
    }
}

