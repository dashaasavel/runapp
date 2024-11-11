package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.utils.assertGrpcCallThrows
import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.grpcmessages.utils.CompetitionRunType
import com.dashaasavel.grpcmessages.utils.PlanServiceMessageWrappers
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Test
    fun `when create 2 marathon plans then error plan already exists`() {
        val userId = authService.registerUser()!!
        planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.PLAN_ALREADY_EXISTS) {
            planService.createAndSaveMarathonPlan(userId)
        }
    }

    @Test
    fun `delete marathon plan`() {
        val userId = authService.registerUser()!!
        var identifier = planService.createAndSaveMarathonPlan(userId).planInfo.identifier

        planService.deletePlan(identifier)
        identifier = planService.getPlan(identifier).planInfo.identifier

        assertTrue {
            identifier.isNull()
        }
    }

    @Test
    fun `delete plan that does not exist (should not throw)`() {
        val userId = authService.registerUser()!!
        val identifier = PlanServiceMessageWrappers.planIdentifier(userId, CompetitionRunType.MARATHON)
        assertDoesNotThrow {
            planService.deletePlan(identifier)
        }
    }
}

