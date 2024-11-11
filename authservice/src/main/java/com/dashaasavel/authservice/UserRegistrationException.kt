package com.dashaasavel.authservice

import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import io.grpc.Status

class UserRegistrationException(
    userRegistrationError: UserRegistrationError
): GrpcServerException(Status.INVALID_ARGUMENT, userRegistrationError)