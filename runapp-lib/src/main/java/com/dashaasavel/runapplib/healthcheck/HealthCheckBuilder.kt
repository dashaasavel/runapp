package com.dashaasavel.runapplib.healthcheck

/**
 * Builder статусной страницы формата json, поставлятся автоконфигурацией
 */
interface HealthCheckBuilder {
    fun ok()
    fun ok(paramName: String, message: String): HealthCheckBuilder
    fun warning(paramName: String, message: String): HealthCheckBuilder
    fun error(paramName: String, message: String): HealthCheckBuilder
}