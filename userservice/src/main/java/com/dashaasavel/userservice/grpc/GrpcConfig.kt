package com.dashaasavel.userservice.grpc

import com.dashaasavel.runapplib.KafkaSender
import com.dashaasavel.runapplib.grpc.core.GrpcServerProperties
import com.dashaasavel.runapplib.grpc.interceptor.ChannelServerInterceptor
import io.grpc.util.MutableHandlerRegistry
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig(
    private val handlerRegistry: MutableHandlerRegistry,
    private val kafkaSender: KafkaSender
) {
    @Bean
    @ConfigurationProperties("grpc.server")
    fun grpcServerProperties() = GrpcServerProperties()

    @Bean
    fun grpcServer() = GrpcServer(grpcServerProperties(), channelServerInterceptor(), kafkaSender, handlerRegistry)

    @Bean
    @ConfigurationProperties("application.security.permitted-channels")
    fun permittedChannels() = ArrayList<String>()

    @Bean
    fun channelServerInterceptor() = ChannelServerInterceptor(permittedChannels())
}