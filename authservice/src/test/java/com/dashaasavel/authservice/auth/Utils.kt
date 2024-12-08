package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.CommonError
import com.dashaasavel.authservice.exceptions.AuthServiceException
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

inline fun assertThrowsAuthServiceException(error: CommonError, executable: () -> Unit) {
    val e = assertThrows<AuthServiceException> {
        executable.invoke()
    }
    assertEquals(error.name, e.message)
    assertEquals(error.status, e.status)
}