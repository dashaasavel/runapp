package com.dashaasavel.runservice.rabbit

import com.dashaasavel.runapplib.logger
import com.dashaasavel.runservice.plan.PlanService
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class DeleteUserMessageListener(
    private val planService: PlanService
): MessageListener {
    private val logger = logger()
    override fun onMessage(message: Message) {
        val userId = String(message.body).toInt()
        planService.deleteAllPlans(userId)
        logger.info("Plan for userId={} successfully deleted", userId)
    }
}