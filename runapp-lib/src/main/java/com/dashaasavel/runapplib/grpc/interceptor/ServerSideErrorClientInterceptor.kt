package com.dashaasavel.runapplib.grpc.interceptor

import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall


class ServerSideErrorClientInterceptor : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        channel: Channel
    ): ClientCall<ReqT, RespT> {
        return object : SimpleForwardingClientCall<ReqT, RespT>(
            channel.newCall(method, callOptions)
        ) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata) {
                super.start(responseListener, headers)
            }
        }
    }
}