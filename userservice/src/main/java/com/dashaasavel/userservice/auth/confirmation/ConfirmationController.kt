package com.dashaasavel.userservice.auth.confirmation

import com.dashaasavel.userservice.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["api/v1/auth"]) // todo в целом прикольно с сочетанием rest и grpc, надо с безопасностью подумать
class ConfirmationController(
    private val userService: UserService
) {
    @GetMapping(path = ["confirm"])
    fun confirmAccount(@RequestParam("token") token: String): String {
        userService.confirmUser(token)
        return "CONFIRMED!"
    }
}