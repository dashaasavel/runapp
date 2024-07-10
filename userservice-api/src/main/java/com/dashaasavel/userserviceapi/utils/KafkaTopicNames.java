package com.dashaasavel.userserviceapi.utils;

public enum KafkaTopicNames{
    GRPC_METRICS("grpc-metrics");

    private final String topicName;
    KafkaTopicNames(String topicName){
        this.topicName = topicName;
    }
    public String getTopicName() {
        return topicName;
    }
}
