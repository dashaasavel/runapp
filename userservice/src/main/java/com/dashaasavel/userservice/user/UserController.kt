package com.dashaasavel.userservice.user

import com.dashaasavel.openapi.api.UsersApi
import com.dashaasavel.openapi.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UsersApi {
    override fun deleteUser(userId: Int): ResponseEntity<Void> {
        userService.deleteUser(userId)
        return ResponseEntity.ok().build()
    }

    override fun getUser(userId: Int): ResponseEntity<User> {
        val user = userService.getUser(userId) ?: return ResponseEntity.notFound().build()
        val respUser = User().apply {
            this.userId = user.id
            this.username = user.username
            this.roles = user.roles?.map { it.name }
        }
        return ResponseEntity.ok().body(respUser)
    }
}