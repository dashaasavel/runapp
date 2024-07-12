package com.dashaasavel.runservice.liquibase

import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
open class LiquibaseConfig(
    private val pgDataSource: DataSource,
) {
//    @Bean
//    open fun liquibaseForPostgres(): SpringLiquibase {
//        val liquibase = SpringLiquibase()
//        liquibase.dataSource = pgDataSource
//        liquibase.changeLog = "classpath:db/changelog/db.changelog-postgres.xml"
//        return liquibase
//    }
}