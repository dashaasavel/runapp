package com.dashaasavel.metricaggregator

import com.dashaasavel.metric.api.GrpcMetric


class Metric(
    var clientId: String,
    var timestamp: Long,
    var metricValue: GrpcMetric
)