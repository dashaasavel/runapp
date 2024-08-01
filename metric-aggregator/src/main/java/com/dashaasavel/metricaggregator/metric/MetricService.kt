package com.dashaasavel.metricaggregator.metric

class MetricService(
    private val metricDAO: MetricDAO
) {
    fun saveMetrics(metrics: List<Metric>) {
        metricDAO.saveMetricBatch(metrics)
    }

    fun getAllMetrics(): List<Metric> {
        return metricDAO.getAllMetrics()
    }
}