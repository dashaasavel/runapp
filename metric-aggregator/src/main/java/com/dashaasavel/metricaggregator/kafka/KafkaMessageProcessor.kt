package com.dashaasavel.metricaggregator.kafka

import com.dashaasavel.metric.api.GrpcMetric
import com.dashaasavel.metricaggregator.metric.Metric
import com.dashaasavel.metricaggregator.metric.MetricService
import com.dashaasavel.runapplib.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.beans.factory.InitializingBean
import java.time.Duration

class KafkaMessageProcessor(
    private val kafkaConsumer: KafkaConsumer<String, GrpcMetric>,
    private val metricService: MetricService
) : InitializingBean {
    private val logger = logger()

    @Volatile
    private var isConsumingMessagesEnabled = false

    private fun processMessages() {
        while (isConsumingMessagesEnabled) {
            val records = kafkaConsumer.poll(Duration.ofMinutes(1)) // блокируемся на #poll()
            if (!records.isEmpty) {
                val metrics: MutableList<Metric> = ArrayList(records.count())
                for (record in records) {
                    logger.info(
                        "Kafka message was received: key={}, record.channel={}, record.serviceAndMethodName={}",
                        record.key(), record.value().channel, record.value().serviceAndMethodName
                    )
                    val metric = Metric(record.key(), record.timestamp(), record.value())
                    metrics += metric
                }
                metricService.saveMetrics(metrics)
                kafkaConsumer.commitSync()
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