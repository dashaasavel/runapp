package com.dashaasavel.userservice

import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import com.dashaasavel.runapplib.grpc.error.UserServiceError
import io.grpc.Status

class UserAlreadyExistsException : GrpcServerException(Status.ALREADY_EXISTS, UserServiceError.USER_ALREADY_EXISTS)