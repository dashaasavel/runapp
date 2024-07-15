package com.dashaasavel.runapplib.grpc.core

class GrpcServerProperties {
    var port: Int = 9090
    var maxInboundMessageSize: Int = 4194304
}

class GrpcExecutorProperties {
    var threadCount = 1
}