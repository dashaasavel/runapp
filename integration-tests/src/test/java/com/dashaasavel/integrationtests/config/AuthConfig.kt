package com.dashaasavel.integrationtests.config

import com.dashaasavel.integrationtests.utils.CallCredentialsClientInterceptor
import com.dashaasavel.integrationtests.utils.LocalStorage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {
    @Bean
    fun localStorage() = LocalStorage()

    @Bean
    fun callCredentialsClientInterceptor() = CallCredentialsClientInterceptor { localStorage().getAuthMetadata() }
}