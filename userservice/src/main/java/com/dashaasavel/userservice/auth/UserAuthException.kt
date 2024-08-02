package com.dashaasavel.userservice.auth

import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils.invalidClientAuthData
import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import com.dashaasavel.runapplib.grpc.error.UserAuthError
import io.grpc.Status

class UserAuthException(
    userAuthError: UserAuthError
): GrpcServerException(Status.UNAUTHENTICATED, invalidClientAuthData(userAuthError))