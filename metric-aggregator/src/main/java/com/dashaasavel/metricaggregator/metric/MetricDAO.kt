package com.dashaasavel.metricaggregator.metric

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement

class MetricDAO(
    private val jdbcTemplate: JdbcTemplate
) {
    private val tableName = "grpc_metrics"
    private val logger = LoggerFactory.getLogger(MetricDAO::class.java)
    fun saveMetricBatch(metrics: List<Metric>) {
        logger.info("Начинаем заварушку!! у нас {} записей", metrics.size)

        jdbcTemplate.batchUpdate("insert into $tableName (timestamp, clientId, value) values (?, ?, ?)", object: BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val e = metrics[i]
                ps.setLong(1, e.timestamp)
                ps.setString(2, e.clientId)
                ps.setBytes(3, e.metricValue.toByteArray())
            }

            override fun getBatchSize(): Int = metrics.size
        })

        logger.info("Закончили заварушку!!")
    }
}