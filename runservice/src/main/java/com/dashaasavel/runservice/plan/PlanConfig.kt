package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.api.UserService
import com.dashaasavel.runservice.training.TrainingsDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class PlanConfig(
    private val mongoTemplate: MongoTemplate,
    private val jdbcTemplate: JdbcTemplate,
    private val userService: UserService
) {
    @Bean
    fun planService() = PlanService(trainingDAO(), userToPlanIdsDAO(), userService)

    @Bean
    fun trainingDAO() = TrainingsDAO(mongoTemplate)

    @Bean
    fun planServiceGrpc() = PlanServiceGrpc(planService())

    @Bean
    fun userToPlanIdsDAO() = PlanInfoDAO(jdbcTemplate)
}