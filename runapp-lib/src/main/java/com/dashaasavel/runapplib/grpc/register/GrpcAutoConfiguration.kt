package com.dashaasavel.runapplib.grpc.register

import com.dashaasavel.runapplib.grpc.core.GrpcExecutorProperties
import com.dashaasavel.runapplib.grpc.core.GrpcServer
import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import com.dashaasavel.runapplib.grpc.interceptor.LogServerInterceptor
import io.grpc.Server
import io.grpc.ServerInterceptor
import io.grpc.util.MutableHandlerRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@ConditionalOnClass(Server::class)
class GrpcAutoConfiguration(
    private val interceptors: List<ServerInterceptor>
) {
    @Bean
    fun grpcServiceBeanPostProcessor() = GrpcServiceBeanPostProcessor(mutableHandlerRegistry())

    @Bean
    fun mutableHandlerRegistry() = MutableHandlerRegistry()

    @Bean
    @ConfigurationProperties("grpc.server")
    fun grpcServerProperties() = GrpcServerProperties()

    @Bean
    @ConfigurationProperties("grpc.executor")
    fun grpcExecutorProperties() = GrpcExecutorProperties()

    @Bean
    fun threadPoolExecutor(): ExecutorService = Executors.newFixedThreadPool(grpcExecutorProperties().threadCount)

    @Bean
    fun grpcServer(): GrpcServer {
        val allInterceptors = mutableListOf<ServerInterceptor>(LogServerInterceptor())
        allInterceptors += interceptors
        return GrpcServer(grpcServerProperties(), allInterceptors, mutableHandlerRegistry(), threadPoolExecutor())
    }
}