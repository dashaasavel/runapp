package com.dashaasavel.runapplib.grpc.error;

import io.grpc.Status;

public class GrpcServerException extends RuntimeException {
    private final Status status;

    private final UserRegistrationResponseError error;

    public GrpcServerException(Status status, UserRegistrationResponseError error) {
        super(error.name());
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public UserRegistrationResponseError getError() {
        return error;
    }
}
