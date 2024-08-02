package com.dashaasavel.userservice.auth.confirmation

import com.dashaasavel.userservice.user.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["v1/auth"])
class ConfirmationController(
    private val userService: UserService
) {
    @PostMapping(path = ["confirm"])
    fun confirmAccount(@RequestParam("token") token: String): String {
        userService.confirmUser(token)
        return "OK!"
    }
}