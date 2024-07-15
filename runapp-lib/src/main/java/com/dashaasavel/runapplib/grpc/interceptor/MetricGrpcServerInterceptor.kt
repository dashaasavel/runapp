package com.dashaasavel.runapplib.grpc.interceptor

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.runapplib.metric.KafkaMetricSender
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import io.grpc.*

class MetricGrpcServerInterceptor(
    private val kafkaSender: KafkaMetricSender
): ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val channel = headers.get(GrpcMetadataUtils.CHANNEL_METADATA_KEY)
        // todo это можно прокинуть из другого перехватчика
        val fullMethodName = call.methodDescriptor.fullMethodName
        val serviceAndMethodName = fullMethodName.substringAfterLast('.')
        val grpcMetric = GrpcMetric.newBuilder().apply {
            this.timestamp = System.currentTimeMillis()
            this.serviceAndMethodName = serviceAndMethodName
            this.channel = channel
        }.build()
        kafkaSender.send(grpcMetric)

        return next.startCall(
            object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {},
            headers
        )
    }
}