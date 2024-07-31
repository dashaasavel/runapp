package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.config.RemoteGrpcConfig
import com.dashaasavel.integrationtests.facades.AuthServiceFacade
import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [RemoteGrpcConfig::class])
@EnableConfigurationProperties
class BaseServiceTest {
    @Autowired
    lateinit var authService: AuthServiceFacade

    @Autowired
    lateinit var userService: UserServiceFacade

    @Autowired
    lateinit var planService: PlanServiceFacade
}