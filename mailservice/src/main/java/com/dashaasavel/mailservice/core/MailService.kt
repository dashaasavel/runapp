package com.dashaasavel.mailservice.core

import com.dashaasavel.runapplib.core.createCachedThreadPoolWithLimit
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.InitializingBean
import org.springframework.mail.javamail.JavaMailSender
import java.util.concurrent.TimeUnit

class MailService(
    private val mailSender: JavaMailSender,
) : InitializingBean {

    private val executor = createCachedThreadPoolWithLimit(2)
    fun sendMessage(mimeMessage: MimeMessage) {
        executor.execute {
            try {
                mailSender.send(mimeMessage)

            } catch (e: Exception) {
                // rerun task
                // нужно пробовать заново послать сообщение, вдруг у нас нет сети
                // мб что-то можно придумать с circuit breaker'ом
            }
        }
    }

    override fun afterPropertiesSet() {
        Runtime.getRuntime().addShutdownHook(Thread {
            // todo надо еще подумать над методами завершения
            executor.awaitTermination(4, TimeUnit.SECONDS)
        })
    }
}