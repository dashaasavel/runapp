package com.dashaasavel.apigateway;

import com.dashaasavel.apigateway.filters.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
//    @Bean
//    public AuthenticationFilter authFilter() {
//        return new AuthenticationFilter();
//    }
//
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("cars-service", r -> r.path("/cars/**")
//                        .filters(f -> f.filter(authFilter()))
//                        .uri("lb://cars-service"))
//
//                .route("userservice", r -> r.path("/users/**")
//                        .filters(f -> f.filter(authFilter()))
//                        .uri("lb://cars-service"))
//
//                .route("runservice", r -> r.path("/plans/**")
//                        .filters(f -> f.filter(authFilter()))
//                        .uri("lb://cars-service"))
//
//                .route("auth-service", r -> r.path("/auth/**")
//                        .filters(f -> f.filter(authFilter()))
//                        .uri("lb://auth-service"))
//                .build();
//    }
}
