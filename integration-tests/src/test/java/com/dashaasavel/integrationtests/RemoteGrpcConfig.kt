package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RemoteGrpcConfig {
    @Bean
    fun userService(): UserServiceGrpc.UserServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(userServiceProperties().hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .build()
        return UserServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    fun userServiceFacade() = UserServiceFacade(userService())

    @Bean
    fun runService(): PlanServiceGrpc.PlanServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(runServiceProperties().hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .build()
        return PlanServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    fun planServiceFacade() = PlanServiceFacade(runService())

    @Bean
    @ConfigurationProperties("remotegrpc.user-service")
    fun userServiceProperties() = GrpcServiceProperties()

    @Bean
    @ConfigurationProperties("remotegrpc.run-service")
    fun runServiceProperties() = GrpcServiceProperties()
}