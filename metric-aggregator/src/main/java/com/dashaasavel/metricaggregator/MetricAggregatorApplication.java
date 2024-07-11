package com.dashaasavel.metricaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(
        exclude = KafkaAutoConfiguration.class
)
public class MetricAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricAggregatorApplication.class, args);
    }

}
