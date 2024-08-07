package com.dashaasavel.runapplib.grpc.core

import com.dashaasavel.runapplib.auth.AuthorizationServerInterceptor
import com.dashaasavel.runapplib.core.logger
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.ServerInterceptor
import io.grpc.util.MutableHandlerRegistry
import org.springframework.context.SmartLifecycle
import java.util.concurrent.ExecutorService

class GrpcServer(
    private val config: GrpcServerProperties,
    private val interceptors: List<ServerInterceptor>,
    private val authorizationServerInterceptor: AuthorizationServerInterceptor?,
    private val handlerRegistry: MutableHandlerRegistry,
    private val executor: ExecutorService
) : SmartLifecycle {
    private val logger = logger()

    private lateinit var server: Server

    @Volatile
    private var isRunning = false

    private fun buildServer(): Server {
        val serverBuilder = ServerBuilder
            .forPort(config.port)
            .fallbackHandlerRegistry(handlerRegistry)
            .maxInboundMessageSize(config.maxInboundMessageSize)
            .executor(executor)
        for (interceptor in interceptors) {
            serverBuilder.intercept(interceptor)
        }
        // отдельно, потому что порядок добавления перехватчиков имеет значения, а он должен отрабатывать первым
        authorizationServerInterceptor?.let { serverBuilder.intercept(it) }
        return serverBuilder.build()
    }

    override fun start() {
        server = buildServer()
        server.start()
        logger.info("Grpc server started on port:{}", config.port)
        isRunning = true
    }

    override fun stop() {
        server.shutdown()
        logger.info("Grpc server was stopped")
        isRunning = false
    }

    override fun isRunning(): Boolean = isRunning
}