package com.dashaasavel.runservice.plan

import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runapplib.grpc.error.GrpcServerException
import io.grpc.Status

class CreatingPlanException(
    error: CreatingPlanError
) : GrpcServerException(Status.INVALID_ARGUMENT, error)