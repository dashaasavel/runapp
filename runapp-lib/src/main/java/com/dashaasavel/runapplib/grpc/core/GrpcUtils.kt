package com.dashaasavel.runapplib.grpc.core

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
        this.onError(e)
        throw e
    }
}

fun com.google.protobuf.GeneratedMessageV3.isNull(): Boolean {
    return this.serializedSize == 0
}