package com.dashaasavel.runservice;

import com.dashaasavel.runapplib.grpc.core.PermittedChannels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RunServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunServiceApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("application.security.permitted-grpc-channels")
    public PermittedChannels permittedGrpcChannels() {
        return new PermittedChannels();
    }
}
