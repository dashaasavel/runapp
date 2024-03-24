package com.dashaasavel.userservice.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("lol/{username}/{password}")
    fun addUser(@PathVariable("username") username: String, @PathVariable("password") password: String) {
        userService.addUser(UserDTO().apply {
            this.username = username
            this.password = password
            this.confirmed = false
        })
    }

    @GetMapping("/id/{id}")
    fun getUser(@PathVariable("id")id: Long) {
        userService.getUser(id)
    }

    @GetMapping("/all")
    fun getUser() {
        userService.getAllUsers()
    }
}