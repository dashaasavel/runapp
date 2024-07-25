package com.dashaasavel.userserviceapi.utils;

public enum RabbitMQQueues {
    USER_DELETION("user_deletion_queue", "user_events_exchange", "user_deletion");

    final String queueName;

    final String exchange;

    final String routingKey;

    RabbitMQQueues(String queueName, String exchange, String routingKey) {
        this.queueName = queueName;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
