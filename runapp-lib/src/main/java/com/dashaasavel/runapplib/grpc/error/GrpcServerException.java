package com.dashaasavel.runapplib.grpc.error;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import static com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils.invalidClientData;

public class GrpcServerException extends StatusRuntimeException {
    public GrpcServerException(Status status, CommonError error) {
        super(status, invalidClientData(error));
    }
}
