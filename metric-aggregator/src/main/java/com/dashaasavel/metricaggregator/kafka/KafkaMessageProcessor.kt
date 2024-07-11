package com.dashaasavel.metricaggregator.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.metricaggregator.metric.Metric
import com.dashaasavel.metricaggregator.metric.MetricDAO
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.beans.factory.InitializingBean
import java.time.Duration

class KafkaMessageProcessor(
    private val kafkaConsumer: KafkaConsumer<String, GrpcMetric>,
    private val metricDAO: MetricDAO
): InitializingBean {
    @Volatile
    private var isConsumingMessagesEnabled = false

    private fun processMessages() {
        while (isConsumingMessagesEnabled) {
            val records = kafkaConsumer.poll(Duration.ofMinutes(1)) // блокируемся на #poll()
            if (!records.isEmpty) {
                val metrics: MutableList<Metric> = ArrayList(records.count())
                for (record in records) {
                    println("RECEIVED MESSAGE: key:${record.key()}, record.channel=${record.value().channel}, record.serviceAndMethodName=${record.value().serviceAndMethodName}")
                    val metric = Metric(record.key(), record.timestamp(), record.value())
                    metrics+= metric
                }
                metricDAO.saveMetricBatch(metrics)
            }
        }
    }

    override fun afterPropertiesSet() {
        isConsumingMessagesEnabled = true
        processMessages()

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                kafkaConsumer.close()
            }
        })
    }
}