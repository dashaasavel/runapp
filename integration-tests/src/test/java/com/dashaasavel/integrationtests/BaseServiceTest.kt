package com.dashaasavel.integrationtests

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [RemoteConfig::class])
@EnableConfigurationProperties
class BaseServiceTest