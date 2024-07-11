package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.training.TrainingsDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class PlanConfig(
    private val mongoTemplate: MongoTemplate,
    private val jdbcTemplate: JdbcTemplate
) {
    @Bean
    open fun planService() = PlanService(trainingDAO(), userToPlanIdsDAO())

    @Bean
    open fun trainingDAO() = TrainingsDAO(mongoTemplate)

    @Bean
    open fun planServiceGrpc() = PlanServiceGrpc(planService())

    @Bean
    open fun userToPlanIdsDAO() = PlanInfoDAO(jdbcTemplate)
}