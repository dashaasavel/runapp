package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
@ContextConfiguration(classes = [RemoteGrpcConfig::class])
@EnableConfigurationProperties
class BaseServiceTest {
    @Autowired
    lateinit var userService: UserServiceFacade

    @Autowired
    lateinit var planService: PlanServiceFacade

    fun registerUserAndCreateMarathonPlan() {
        val userId = userService.registerUser()

        val planInfo = planService.createMarathonPlanInfo(userId)

        val trainingList = planService.createPlan(planInfo).trainingsList

        assertNotEquals(0, trainingList.size)

        val planIdentifier = planService.createMarathonPlanIdentifier(userId)
        planService.savePlan(planIdentifier)

        val savedPlan = planService.getPlan(planIdentifier)

        assertEquals(planInfo, savedPlan.planInfo)
        assertNotEquals(0, trainingList.size)
    }
}