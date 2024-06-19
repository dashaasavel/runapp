package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RemoteConfig {
    @Bean
    open fun userService(): UserServiceGrpc.UserServiceBlockingStub {
        val channel = ManagedChannelBuilder
//            .forTarget(userServiceProperties().hostAndPort)
            .forTarget("localhost:9091")
            .usePlaintext()
            .build()
        return UserServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    open fun runService(): PlanServiceGrpc.PlanServiceBlockingStub {
        val channel = ManagedChannelBuilder
//            .forTarget(userServiceProperties().hostAndPort)
            .forTarget("localhost:9092")
            .usePlaintext()
            .build()
        return PlanServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    @ConfigurationProperties("remotegrpc.user-service")
    open fun userServiceProperties() = GrpcServiceProperties()
}