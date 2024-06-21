package com.dashaasavel.runapplib.grpc.interceptor

import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import io.grpc.*

/**
 * Серверный перехватчик grpc-вызовов для валидации канала клиента
 */
class ChannelServerInterceptor(
    private val permittedChannels: List<String>
) : ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val channel = headers.get(GrpcMetadataUtils.CHANNEL_METADATA_KEY)
        if (!permittedChannels.contains(channel)) {
            call.close(Status.PERMISSION_DENIED, GrpcMetadataUtils.invalidChannelMetadata(channel))
        }
        return next.startCall(object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {}, headers)
    }
}