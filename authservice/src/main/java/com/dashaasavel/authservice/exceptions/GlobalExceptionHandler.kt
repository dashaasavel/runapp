package com.dashaasavel.authservice.exceptions

import com.dashaasavel.authservice.ErrorMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val objectMapper = ObjectMapper()

    @ExceptionHandler(AuthServiceException::class)
    fun handleDuplicateKeyException(e: AuthServiceException): ResponseEntity<String> {
        val errorMessage = objectMapper.writeValueAsString(ErrorMessage(e.message))
        return ResponseEntity.status(e.status).body(errorMessage)
    }
}