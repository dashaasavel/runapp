package com.dashaasavel.userservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DockerHealthCheckController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }
}
