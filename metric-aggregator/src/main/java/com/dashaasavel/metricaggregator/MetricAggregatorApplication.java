package com.dashaasavel.metricaggregator;

import com.dashaasavel.runapplib.grpc.GrpcServerAutoConfiguration;
import com.dashaasavel.runapplib.metric.KafkaMetricAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(
        exclude = {
                KafkaAutoConfiguration.class,
                KafkaMetricAutoConfiguration.class,
                GrpcServerAutoConfiguration.class
        }
)
public class MetricAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricAggregatorApplication.class, args);
    }

}
