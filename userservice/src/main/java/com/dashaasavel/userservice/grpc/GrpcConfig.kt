package com.dashaasavel.userservice.grpc

import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceImplBase
import io.grpc.util.MutableHandlerRegistry
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GrpcConfig(
    private val handlerRegistry: MutableHandlerRegistry
) {
    @Bean
    @ConfigurationProperties("grpc.server")
    open fun grpcServerProperties() = GrpcServerProperties()

    @Bean
    open fun grpcServer() = GrpcServer(grpcServerProperties(), handlerRegistry)
}