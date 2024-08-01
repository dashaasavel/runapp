package com.dashaasavel.metricaggregator.metric

import com.dashaasavel.metric.api.GrpcMetric
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

object MetricRowMapper: RowMapper<Metric> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Metric? {
        return if (rs.next()) {
            val id = rs.getInt("id")
            val clientId = rs.getString("clientId")
            val timestamp = rs.getTimestamp("timestamp").time
            val metricValue = GrpcMetric.parseFrom(rs.getBytes("value"))
            val metric = Metric(clientId, timestamp, metricValue).apply {
                this.id = id
            }
            metric
        } else null
    }
}