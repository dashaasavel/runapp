package com.dashaasavel.integrationtests.config

import com.dashaasavel.integrationtests.utils.CallCredentialsClientInterceptor
import com.dashaasavel.integrationtests.utils.LocalStorage
import com.dashaasavel.integrationtests.facades.AuthServiceFacade
import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.runservice.api.PlanServiceGrpc
import com.dashaasavel.userservice.api.AuthServiceGrpc
import com.dashaasavel.userservice.api.UserServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(AuthConfig::class)
@Configuration
class RemoteGrpcConfig(
    private val localStorage: LocalStorage,
    private val callCredentialsClientInterceptor: CallCredentialsClientInterceptor
) {
    @Bean
    fun userService(): UserServiceGrpc.UserServiceBlockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel())

    @Bean
    fun authService(): AuthServiceGrpc.AuthServiceBlockingStub = AuthServiceGrpc.newBlockingStub(userServiceChannel())

    @Bean
    fun userServiceChannel(): ManagedChannel = ManagedChannelBuilder
        .forTarget(userServiceProperties().hostAndPort)
        .usePlaintext()
        .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
        .intercept(callCredentialsClientInterceptor)
        .build()

    @Bean
    fun authServiceFacade() = AuthServiceFacade(authService(), localStorage)

    @Bean
    fun userServiceFacade() = UserServiceFacade(userService())

    @Bean
    fun runService(): PlanServiceGrpc.PlanServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(runServiceProperties().hostAndPort)
            .usePlaintext()
            .intercept(GrpcMetadataUtils.clientChannelAttachingInterceptor("TEST"))
            .intercept(callCredentialsClientInterceptor)
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