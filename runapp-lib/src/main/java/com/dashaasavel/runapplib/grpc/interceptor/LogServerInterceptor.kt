package com.dashaasavel.runapplib.grpc.interceptor

import io.grpc.ForwardingServerCall
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor


class LogServerInterceptor : ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        requestHeaders: io.grpc.Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val fullMethodName = call.methodDescriptor.fullMethodName
        val serviceAndMethod = fullMethodName.substringAfterLast('.')
        println("Service method called: $serviceAndMethod")
        return next.startCall(
            object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {},
            requestHeaders
        )
    }
}