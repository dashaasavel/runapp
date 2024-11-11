package com.dashaasavel.authservice.api

import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiConfig {
    @Bean
    fun userServiceFacade() = UserServiceFacade(userServiceGrpc())

    @Bean
    fun userServiceGrpc(): UserServiceGrpc.UserServiceBlockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel())

    @Bean
    fun userServiceChannel(): ManagedChannel = ManagedChannelBuilder
        .forTarget(userServiceProperties().hostAndPort)
        .usePlaintext()
        .build()

    @Bean
    @ConfigurationProperties("remotegrpc.user-service")
    fun userServiceProperties() = GrpcServiceProperties()
}