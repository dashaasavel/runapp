package com.dashaasavel.userservice.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docker")
public class DockerHealthCheckController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }
}
