package com.dashaasavel.runservice.grpc

import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import io.grpc.util.MutableHandlerRegistry
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig(
    private val handlerRegistry: MutableHandlerRegistry
) {
    @Bean
    @ConfigurationProperties("grpc.server")
    fun grpcServerProperties() = GrpcServerProperties()

    @Bean
    fun grpcServer() = GrpcServer(grpcServerProperties(), handlerRegistry)
}