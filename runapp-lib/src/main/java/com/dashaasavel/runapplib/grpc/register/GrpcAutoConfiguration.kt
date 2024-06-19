package com.dashaasavel.runapplib.grpc.register

import io.grpc.util.MutableHandlerRegistry

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GrpcAutoConfiguration {
    @Bean
    open fun grpcServiceBeanPostProcessor() = GrpcServiceBeanPostProcessor(mutableHandlerRegistry())

    @Bean
    open fun mutableHandlerRegistry() = MutableHandlerRegistry()
}