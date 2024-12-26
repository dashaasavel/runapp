package com.dashaasavel.runservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@SpringBootApplication(exclude = {
        RabbitAutoConfiguration.class
})
public class RunServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunServiceApplication.class, args);
    }
}
