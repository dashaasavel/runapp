package com.dashaasavel.integrationtests

import com.dashaasavel.openapi.model.User
import org.springframework.boot.test.web.client.TestRestTemplate
import kotlin.test.assertEquals

class UserRestTemplate(
    private val restTemplate: TestRestTemplate
) {
    var hostAndPort: String = ""
    fun deleteUser(userId: Int) {
        restTemplate.delete(hostAndPort + "users/", userId)
    }

    fun getUser(userId: Int): User? {
        val response = restTemplate.getForEntity(hostAndPort + "users/", User::class.java, userId)
        assertEquals(200, response.statusCode.value())
        return response.body

    }
}