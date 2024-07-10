package com.dashaasavel.metricaggregator.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.metricaggregator.Metric
import com.dashaasavel.metricaggregator.db.MetricDAO
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.beans.factory.InitializingBean
import org.springframework.jmx.export.annotation.ManagedAttribute
import java.time.Duration

class KafkaMessageProcessor(
    private val kafkaConsumer: KafkaConsumer<String, GrpcMetric>,
    private val metricDAO: MetricDAO
): InitializingBean {
    @Volatile
//    @get:ManagedAttribute
//    @set:ManagedAttribute
    private var isConsumingMessagesEnabled = false

    private fun processMessages() {
        while (isConsumingMessagesEnabled) {
            val records = kafkaConsumer.poll(Duration.ofMinutes(1)) // блокируемся на #poll()
            if (!records.isEmpty) {
                val metrics: MutableList<Metric> = ArrayList(records.count())
                for (record in records) {
                    val metric = Metric(record.key(), record.timestamp(), record.value())
                    metrics+= metric
                }
                metricDAO.saveMetricBatch(metrics)
            }
        }
        kafkaConsumer.close()
    }

    override fun afterPropertiesSet() {
        isConsumingMessagesEnabled = true
        processMessages()
    }
}