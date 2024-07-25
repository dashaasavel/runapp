package com.dashaasavel.userservice.rabbit

import com.dashaasavel.runapplib.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RabbitController(
    private val template: UserDeletionNotificator
) {
    private val logger = logger()

    private var counter = 1

    @GetMapping("/send")
    fun sendMessageToRabbit() {
        val userId = counter++
        template.notify(userId)
        logger.info("Message with userId=$userId was sent")
    }

    @GetMapping("/health")
    fun healthCheck(): String {
        return "OK"
    }
}