package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.config.RemoteConfig
import com.dashaasavel.integrationtests.facades.AuthServiceFacade
import com.dashaasavel.integrationtests.facades.PlanServiceFacade
import com.dashaasavel.integrationtests.facades.UserServiceFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [RemoteConfig::class])
@EnableConfigurationProperties
class BaseServiceTest {
    @Autowired
    lateinit var authService: AuthServiceFacade

    @Autowired
    lateinit var userService: UserServiceFacade

    @Autowired
    lateinit var planService: PlanServiceFacade

    @Autowired
    lateinit var restTemplate: TestRestTemplate
}