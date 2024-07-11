package com.dashaasavel.runservice.liquibase

import liquibase.ext.mongodb.database.MongoClientDriver
import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import javax.sql.DataSource

@Configuration
open class LiquibaseConfig(
    private val pgDataSource: DataSource,
    private val mongoProperties: MongoProperties
) {
    @Bean
    open fun liquibaseForPostgres(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = pgDataSource
        liquibase.changeLog = "classpath:db/changelog/db.changelog-postgres.xml"
        return liquibase
    }

    @Bean
    open fun liquibaseForMongoDB(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = SimpleDriverDataSource(MongoClientDriver(), mongoProperties.uri)
        liquibase.changeLog = "classpath:db/changelog/db.changelog-mongodb.xml"
        return liquibase
    }
}