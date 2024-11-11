package com.dashaasavel.grpcmessages.utils;

public enum RabbitMQQueues {
    USER_DELETED_MAIL_SERVICE("user-deleted-queue-mail-service", "user.deleted", "user.event.deleted"),
    USER_DELETED_RUN_SERVICE("user-deleted-queue-run-service", "user.deleted", "user.event.deleted"),
    USER_NEED_CONFIRMATION_MAIL_SERVICE("user-unconfirmed-queue-mail-service", "user.unconfirmed", "user.event.unconfirmed"),
    USER_CREATED_MAIL_SERVICE("user-created-queue-mail-service", "user.created", "user.event.created");

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
