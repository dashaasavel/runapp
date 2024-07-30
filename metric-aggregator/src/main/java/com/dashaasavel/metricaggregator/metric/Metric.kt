package com.dashaasavel.metricaggregator.metric

import com.dashaasavel.metric.api.GrpcMetric


class Metric(
    var clientId: String,
    var timestamp: Long,
    var metricValue: GrpcMetric
) {
    var id: Int? = null
}