package com.dashaasavel.authservice.exceptions

import com.dashaasavel.authservice.CommonError
import org.springframework.http.HttpStatus

class AuthServiceException(error: CommonError) : RuntimeException(error.getName()) {
    val status: HttpStatus = error.status
}