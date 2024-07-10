package com.dashaasavel.runapplib

import com.dashaasavel.metric.api.GrpcMetric

interface KafkaSender {
    fun send(grpcMetric: GrpcMetric)
}