package com.dashaasavel.authservice.auth

import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils.invalidClientAuthData
import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import com.dashaasavel.runapplib.grpc.error.AuthError
import io.grpc.Status

class UserAuthException(
    userAuthError: AuthError
): GrpcServerException(Status.UNAUTHENTICATED, invalidClientAuthData(userAuthError))