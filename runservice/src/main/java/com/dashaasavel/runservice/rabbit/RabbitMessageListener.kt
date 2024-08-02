package com.dashaasavel.runservice.rabbit

import com.dashaasavel.mailservice.message.GoodByeMessage
import com.dashaasavel.runapplib.logger
import com.dashaasavel.runservice.plan.PlanService
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class RabbitMessageListener(
    private val planService: PlanService
): MessageListener {
    private val logger = logger()
    override fun onMessage(message: Message) {
        val deletedUser = GoodByeMessage.parseFrom(message.body)
        val userId = deletedUser.userId
        val username = deletedUser.userInfo.username

        logger.info("Message with deleted userId={}, username={} was received", userId, username)
        val trainingIds = planService.deleteAllPlans(userId)
        logger.info("Plans with trainingIds={} for userId={} successfully deleted", userId, trainingIds)
    }
}