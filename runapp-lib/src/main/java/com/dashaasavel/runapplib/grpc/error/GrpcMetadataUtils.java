package com.dashaasavel.runapplib.grpc.error;

import io.grpc.ClientInterceptor;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

import javax.annotation.Nullable;

public class GrpcMetadataUtils {
    public static io.grpc.Metadata.Key<String> ERROR_METADATA_KEY = io.grpc.Metadata.Key.of("error", io.grpc.Metadata.ASCII_STRING_MARSHALLER);

    public static io.grpc.Metadata ERROR_METADATA = new io.grpc.Metadata();

    public static Context.Key<String> ERROR_CONTEXT_KEY = Context.key("x-error");

    public static io.grpc.Metadata.Key<String> CHANNEL_METADATA_KEY = io.grpc.Metadata.Key.of("x-channel", io.grpc.Metadata.ASCII_STRING_MARSHALLER);

    public static Context.Key<String> CHANNEL_CONTEXT_KEY = Context.key("x-channel");

    public static Metadata invalidChannelMetadata(@Nullable String channel) {
        Metadata metadata = new Metadata();
        if (channel == null) {
            metadata.put(ERROR_METADATA_KEY, "Channel should be specified");
        } else {
            metadata.put(ERROR_METADATA_KEY, "Invalid channel: " + channel);
        }
        return metadata;
    }

    public static ClientInterceptor clientHeaderAttachingInterceptor(String channelName) {
        Metadata headers = new Metadata();
        headers.put(CHANNEL_METADATA_KEY, channelName);
        return MetadataUtils.newAttachHeadersInterceptor(headers);
    }
}
