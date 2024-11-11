package com.dashaasavel.integrationtests.config

import com.dashaasavel.integrationtests.AuthRestTemplate
import com.dashaasavel.integrationtests.UserRestTemplate
import com.dashaasavel.integrationtests.facades.AuthServiceFacade
import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import com.dashaasavel.runapplib.grpc.core.GrpcServiceProperties
import com.dashaasavel.runservice.api.PlanServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RemoteConfig(
    private val restTemplate: TestRestTemplate
) {
    @Bean
    @ConfigurationProperties("authservice.rest")
    fun authRestTemplate() = AuthRestTemplate(restTemplate)

    @Bean
    @ConfigurationProperties("userservice.rest")
    fun userRestTemplate() = UserRestTemplate(restTemplate)

    @Bean
    fun authServiceFacade() = AuthServiceFacade(authRestTemplate())

    @Bean
    fun userServiceFacade() = UserServiceFacade(userRestTemplate())

    @Bean
    fun runService(): PlanServiceGrpc.PlanServiceBlockingStub {
        val channel = ManagedChannelBuilder
            .forTarget(runServiceProperties().hostAndPort)
            .usePlaintext()
            .build()
        return PlanServiceGrpc.newBlockingStub(channel)
    }

    @Bean
    fun planServiceFacade() = PlanServiceFacade(runService())

    @Bean
    @ConfigurationProperties("remotegrpc.run-service")
    fun runServiceProperties() = GrpcServiceProperties()
}