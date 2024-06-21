package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RemoteConfig {
    @Bean
    open fun userService(userServiceProperties: GrpcServiceProperties): UserServiceGrpc.UserServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(userServiceProperties.hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .build()
        return UserServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    open fun runService(runServiceProperties: GrpcServiceProperties): PlanServiceGrpc.PlanServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(runServiceProperties.hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .build()
        return PlanServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    @ConfigurationProperties("remotegrpc.user-service")
    open fun userServiceProperties() = GrpcServiceProperties()

    @Bean
    @ConfigurationProperties("remotegrpc.run-service")
    open fun runServiceProperties() = GrpcServiceProperties()
}