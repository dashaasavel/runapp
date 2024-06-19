package com.dashaasavel.runservice.plan

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

@Configuration
open class DAOConfiguration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    open fun postgreSQLContainer() = PostgreSQLContainer<Nothing>("postgres:15")

    @Bean
    open fun dataSource(): DataSource = HikariDataSource().apply {
        this.jdbcUrl = postgreSQLContainer().jdbcUrl
        this.username = postgreSQLContainer().username
        this.password = postgreSQLContainer().password
    }
}