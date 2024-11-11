package com.dashaasavel.integrationtests.facades

import com.dashaasavel.integrationtests.UserRestTemplate
import com.dashaasavel.openapi.model.User

class UserServiceFacade(
    private val restTemplate: UserRestTemplate
) {
    fun getUserById(userId: Int): User? {
        return restTemplate.getUser(userId)
    }

    fun deleteUserById(userId: Int) {
        restTemplate.deleteUser(userId)
    }
}