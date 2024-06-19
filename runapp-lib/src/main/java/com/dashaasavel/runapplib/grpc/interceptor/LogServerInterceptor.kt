package com.dashaasavel.runapplib.grpc.interceptor

import io.grpc.ForwardingServerCall
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor


class LogServerInterceptor: ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        requestHeaders: io.grpc.Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val fullMethodName = call.methodDescriptor.fullMethodName
        val serviceAndMethod = fullMethodName.substring(fullMethodName.lastIndexOf('.') + 1)
        println("LOG: $fullMethodName")
        return next.startCall(object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {}, requestHeaders)
    }
}