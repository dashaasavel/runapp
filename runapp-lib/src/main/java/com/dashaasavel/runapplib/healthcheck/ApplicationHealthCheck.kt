package com.dashaasavel.runapplib.healthcheck

import org.springframework.boot.info.BuildProperties
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApplicationHealthCheck(
    buildProperties: BuildProperties
): HealthCheck {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val startTime = LocalDateTime.now()
    private val startTimeFormatted = startTime.format(dateTimeFormatter)

    private val version = buildProperties.version
    private val name = buildProperties.name
    override fun processHealthCheck(builder: HealthCheckBuilder) {
        builder.ok("name", name)
            .ok("version", version)
//            .ok("branch", "staging")
            .ok("uptime", "TODO") // в днях - часах - минутах - секундах
            .ok("start time", startTimeFormatted)
    }
}