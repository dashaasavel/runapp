package com.dashaasavel.runapplib.grpc.error;

public enum UserRegistrationResponseError {
    USER_EXISTS_AND_CONFIRMED,
    NEED_TO_CONFIRM_ACCOUNT,
    NEW_TOKEN_WAS_SENT
}
