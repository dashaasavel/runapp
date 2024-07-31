package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.utils.assertGrpcCallThrows
import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import com.dashaasavel.userserviceapi.utils.PlanServiceMessageWrappers
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Test
    fun `create 2 marathon plans (throw exception)`() {
        val userId = authService.registerAndAuthUser()
        planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createAndSaveMarathonPlan(userId)
        }
    }

    @Test
    fun `delete marathon plan (success)`() {
        val userId = authService.registerAndAuthUser()
        var identifier = planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        planService.deletePlan(identifier)
        identifier = planService.getPlan(identifier).planInfo.identifier

        assertTrue {
            identifier.isNull()
        }
    }

    @Test
    fun `delete plan that does not exist (should not throw)`() {
        val userId = authService.registerAndAuthUser()
        val identifier = PlanServiceMessageWrappers.planIdentifier(userId, CompetitionRunType.MARATHON)
        assertDoesNotThrow {
            planService.deletePlan(identifier)
        }
    }
}

