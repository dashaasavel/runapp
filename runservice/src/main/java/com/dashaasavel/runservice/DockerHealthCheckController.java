package com.dashaasavel.runservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerHealthCheckController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }
}
