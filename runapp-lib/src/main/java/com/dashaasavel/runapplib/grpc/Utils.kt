package com.dashaasavel.runapplib.grpc

import io.grpc.ServerCall

fun <ReqT, RespT> ServerCall<ReqT, RespT>.getServiceAndMethodName(): String = this.methodDescriptor.fullMethodName.substringAfterLast('.')