package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.error.CommonError
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import io.grpc.StatusRuntimeException
import kotlin.test.assertEquals

inline fun <reified T : StatusRuntimeException> assertGrpcCallThrows(error: CommonError, executable: () -> Unit) {
    val trailers = org.junit.jupiter.api.assertThrows<T> {
        executable.invoke()
    }.trailers!!
    assertEquals(error.getName(), trailers[GrpcMetadataUtils.ERROR_METADATA_KEY])
}