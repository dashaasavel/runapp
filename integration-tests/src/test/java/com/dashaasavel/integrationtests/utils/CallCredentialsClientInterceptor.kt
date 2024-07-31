package com.dashaasavel.integrationtests.utils

import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener

/**
 * Перехватчик для добавления JWT-токена к каждому grpc-вызову в тестах
 */
class CallCredentialsClientInterceptor(
    private val bearerTokenSupplier: () -> Metadata?
) : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                val metadata = bearerTokenSupplier.invoke()
                if (metadata != null) {
                    headers.merge(metadata)
                }
                super.start(object : SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onHeaders(headers: Metadata) {
                        super.onHeaders(headers)
                    }
                }, headers)
            }
        }
    }
}