package com.dashaasavel.apigateway;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register"
    );

    public static Predicate<ServerHttpRequest> isSecured =
            request -> !openApiEndpoints.contains(request.getURI().getPath()); // как будто если есть параметры, то будет мимо
}