package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class UserServiceIT : BaseServiceTest() {
    @Test
    fun `register user with existing username`() {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"

        val userId = userService.registerUser(username, password)

        val user = userService.getUserById(userId)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        assertGrpcCallThrows<StatusRuntimeException>(UserRegistrationError.USER_EXISTS_AND_CONFIRMED) {
            userService.registerUser(username, password)
        }
    }

    @Test
    fun `register and delete user by id`() {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"

        val userId = userService.registerUser(username, password)

        var user = userService.getUserById(userId)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        userService.deleteUserById(userId)

        user = userService.getUserById(userId)

        assertTrue {
            user.isNull()
        }
    }

    @Test
    fun `register and delete user by username`() {
        val username = "test-user-${Random().nextInt() % 5000}@gmail.com"
        val password = "password-${Random().nextInt() % 5000}"

        val userId = userService.registerUser(username, password)

        var user = userService.getUserByUsername(username)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        userService.deleteUserByUsername(username)

        user = userService.getUserByUsername(username)

        assertTrue {
            user.isNull()
        }
    }

    @Test
    fun `delete user and check if plans deleted too`() {
        val userId = userService.registerUser()
        val planInfo = planService.createAndSaveMarathonPlan(userId).planInfo
        val identifier = planInfo.identifier

        userService.deleteUserById(userId)
        Thread.sleep(10000)
        val plan = planService.getPlan(identifier)

        // FIXME: check results with retries (because rabbit)
        assertTrue {
            plan.isNull()
        }
    }
}