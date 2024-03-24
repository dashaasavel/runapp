package com.dashaasavel.userservice.auth

import com.dashaasavel.userservice.role.Roles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(
    private val registrationService: RegistrationService
) {
    @GetMapping("/v1/register/{username}/{password}")
    fun register(@PathVariable("username")username: String, @PathVariable("password")password: String) {
        registrationService.registerAccount(username, password, listOf(Roles.USER))
    }

    @GetMapping("/v1/confirm/{token}")
    fun confirm(@PathVariable("token")token: String) {
        registrationService.confirmAccount(token)
    }
}
