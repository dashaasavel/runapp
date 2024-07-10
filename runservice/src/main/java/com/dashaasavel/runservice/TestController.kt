package com.dashaasavel.runservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["test"])
class TestController {
    @GetMapping
    fun test(): String {
        return "OK"
    }
}