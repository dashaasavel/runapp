package com.dashaasavel.integrationtests

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [RemoteConfig::class])
open class BaseServiceTest