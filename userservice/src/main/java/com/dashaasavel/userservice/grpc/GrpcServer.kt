package com.dashaasavel.userservice.grpc

import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import com.dashaasavel.runapplib.grpc.interceptor.LogServerInterceptor
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.ServerInterceptor
import io.grpc.util.MutableHandlerRegistry
import org.springframework.context.SmartLifecycle

class GrpcServer(
    private val config: GrpcServerProperties,
    private val channelInterceptor: ServerInterceptor,
    private val handlerRegistry: MutableHandlerRegistry
) : SmartLifecycle {
    private lateinit var server: Server
    private var isRunning = false

    private fun buildServer(): Server {
        // TODO ACCESS LOG
        // TODO GRAFANA INTERCEPTOR
        // todo executor
        return ServerBuilder
            .forPort(config.port)
            .fallbackHandlerRegistry(handlerRegistry)
//            .addService(userService)
            .intercept(LogServerInterceptor())
            .intercept(channelInterceptor)
//            .intercept(GlobalGrpcInterceptor())
            .maxInboundMessageSize(config.maxInboundMessageSize)
            .build()
    }

    override fun start() {
        server = buildServer()
        server.start()
        println("Grpc server running on port ${config.port}")
        isRunning = true
    }

    override fun stop() {
        server.shutdown()
        isRunning = false
    }

    override fun isRunning(): Boolean = isRunning
}