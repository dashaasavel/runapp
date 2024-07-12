package com.dashaasavel.runapplib.grpc.register

import io.grpc.util.MutableHandlerRegistry

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcAutoConfiguration {
    @Bean
    fun grpcServiceBeanPostProcessor() = GrpcServiceBeanPostProcessor(mutableHandlerRegistry())

    @Bean
    fun mutableHandlerRegistry() = MutableHandlerRegistry()
}