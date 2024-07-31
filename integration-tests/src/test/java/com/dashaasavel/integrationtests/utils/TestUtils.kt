package com.dashaasavel.integrationtests.utils

import com.dashaasavel.runapplib.auth.AuthConstants
import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

inline fun <reified T : StatusRuntimeException> assertGrpcCallThrows(error: CommonError, executable: () -> Unit) {
    val trailers = assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[GrpcMetadataUtils.ERROR_METADATA_KEY])
}

inline fun <reified T : StatusRuntimeException> assertGrpcCallThrowsAuthException(error: CommonError, executable: () -> Unit) {
    val trailers = assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[AuthConstants.AUTH_ERROR])
}