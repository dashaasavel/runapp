package com.dashaasavel.runapplib.grpc.core

import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils.ERROR_METADATA_KEY
import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import io.grpc.Metadata
import io.grpc.stub.StreamObserver

/**
 * @param block - function that able to throw an RuntimeException
 */
fun <T> StreamObserver<T>.reply(block: () -> T) {
    try {
        val value = block.invoke()
        this.onNext(value)
        this.onCompleted()
    } catch (e: RuntimeException) {
        when(e) {
            is GrpcServerException -> {
                val metadata = Metadata()
                metadata.put(ERROR_METADATA_KEY, e.error.name)
                this.onError(e.status.asRuntimeException())
                throw e
            }
        }
        this.onError(e)
        throw e
    }
}