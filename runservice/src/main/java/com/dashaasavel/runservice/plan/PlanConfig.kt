package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.training.TrainingsDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class PlanConfig(
    private val mongoTemplate: MongoTemplate,
    private val jdbcTemplate: JdbcTemplate,
    private val transactionTemplate: TransactionTemplate
) {
    @Bean
    fun planService() = PlanService(trainingDAO(), userToPlanIdsDAO(), transactionTemplate)

    @Bean
    fun trainingDAO() = TrainingsDAO(mongoTemplate)

    @Bean
    fun planServiceGrpc() = PlanServiceGrpc(planService())

    @Bean
    fun userToPlanIdsDAO() = PlanInfoDAO(jdbcTemplate)
}