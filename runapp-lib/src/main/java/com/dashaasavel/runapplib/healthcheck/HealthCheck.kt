package com.dashaasavel.runapplib.healthcheck

interface HealthCheck {
    fun processHealthCheck(builder: HealthCheckBuilder)
}