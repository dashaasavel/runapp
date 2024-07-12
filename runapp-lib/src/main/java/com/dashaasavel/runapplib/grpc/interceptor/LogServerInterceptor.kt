package com.dashaasavel.runapplib.grpc.interceptor

import com.dashaasavel.runapplib.logger
import io.grpc.ForwardingServerCall
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor


class LogServerInterceptor : ServerInterceptor {
    private val logger = logger()
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: io.grpc.Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val fullMethodName = call.methodDescriptor.fullMethodName
        val serviceAndMethod = fullMethodName.substringAfterLast('.')
        logger.info("TEST: service method called: {}", serviceAndMethod)
        return next.startCall(
            object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {},
            headers
        )
    }
}