package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class UserServiceIT : BaseServiceTest() {
    @Test
    fun `register user with existing username`() {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"

        authService.registerUser(username, password)!!

        val nullUserId = authService.registerUser(username, password)
        assertNull(nullUserId)
    }

    @Test
    fun `register and delete user by id`() {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"

        val userId = 1
//        val userId = authService.registerUser(username, password)!!

        var user = userService.getUserById(userId)

        assertNotNull(user)

        userService.deleteUserById(userId)
        user = userService.getUserById(userId)

        assertNull(user)
    }

    @Test
    fun `when delete user then plans should be deleted too`() {
        val userId = authService.registerUser()
        val planInfo = planService.createAndSaveMarathonPlan(userId!!).planInfo
        val identifier = planInfo.identifier

        userService.deleteUserById(userId)
        Thread.sleep(10000)
        val plan = planService.getPlan(identifier)

        // FIXME: check results with retries (because rabbit)
        assertTrue {
            plan.isNull()
        }
    }

    @Test
    fun `rest test delete`() {
//        `when`().request(Method.DELETE, "localhost:8083/users/1").then().statusCode(200)
//        userService.deleteUserById(1)
        restTemplate.delete("http://localhost:8083/users/1", 1)
    }
}