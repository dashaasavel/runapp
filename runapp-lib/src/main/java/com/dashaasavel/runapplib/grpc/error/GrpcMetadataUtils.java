package com.dashaasavel.runapplib.grpc.error;

import io.grpc.Metadata;

import javax.annotation.Nullable;

public class GrpcMetadataUtils {
    public static io.grpc.Metadata.Key<String> ERROR_METADATA_KEY = io.grpc.Metadata.Key.of("runapp-error", io.grpc.Metadata.ASCII_STRING_MARSHALLER);

    public static io.grpc.Metadata.Key<String> CHANNEL_METADATA_KEY = io.grpc.Metadata.Key.of("x-channel", io.grpc.Metadata.ASCII_STRING_MARSHALLER);

    public static Metadata invalidChannelMetadata(@Nullable String channel) {
        if (channel == null) {
            return invalidClientData(ChannelError.CHANNEL_CANNOT_BE_NULL);
        } else {
            return invalidClientData(ChannelError.CHANNEL_IS_NOT_PERMITTED);
        }
    }

    public static Metadata invalidClientData(CommonError error) {
        Metadata metadata = new Metadata();
        metadata.put(ERROR_METADATA_KEY, error.getName());
        return metadata;
    }
}
