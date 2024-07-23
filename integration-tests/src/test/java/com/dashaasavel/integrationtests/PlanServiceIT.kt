package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlanServiceIT : BaseServiceTest() {
    @Test
    fun `create marathon plan with non existing user`() {
        assertGrpcCallThrows<StatusRuntimeException>(CreatingPlanError.USER_DOES_NOT_EXIST) {
            planService.createAndSaveMarathonPlan(Random().nextInt() % 5000)
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

