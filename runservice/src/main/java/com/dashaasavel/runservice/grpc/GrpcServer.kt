package com.dashaasavel.runservice.grpc

import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import com.dashaasavel.runapplib.grpc.interceptor.LogServerInterceptor
import com.dashaasavel.runapplib.logger
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.util.MutableHandlerRegistry
import org.springframework.context.SmartLifecycle

class GrpcServer(
    private val config: GrpcServerProperties,
    private val handlerRegistry: MutableHandlerRegistry
): SmartLifecycle {
    private val logger = logger()

    private lateinit var server : Server
    @Volatile
    private var isRunning = false

    private fun buildServer(): Server {
        // TODO ACCESS LOG
        // TODO GRAFANA INTERCEPTOR
        // todo executor
        return ServerBuilder
            .forPort(config.port)
            .fallbackHandlerRegistry(handlerRegistry)
            .intercept(LogServerInterceptor())
            .maxInboundMessageSize(config.maxInboundMessageSize)
            .build()
    }

    override fun start() {
        server = buildServer()
        server.start()
        logger.info("Grpc server started on port:{}", config.port)
        isRunning = true
    }

    override fun stop() {
        server.shutdown()
        logger.info("Grpc server was stopped on port")
        isRunning = false
    }

    override fun isRunning(): Boolean = isRunning
}