package com.dashaasavel.runservice.api

import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RemoteGrpcConfig {
    @Bean
    fun userService() = UserService(userServiceBlockingStub())

    @Bean
    fun userServiceBlockingStub(): UserServiceGrpc.UserServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(userServiceProperties().hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .build()
        return UserServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    @ConfigurationProperties("remotegrpc.user-service")
    fun userServiceProperties() = GrpcServiceProperties()
}