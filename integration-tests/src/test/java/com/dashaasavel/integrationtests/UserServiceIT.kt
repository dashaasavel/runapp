package com.dashaasavel.integrationtests

import com.dashaasavel.runapplib.grpc.core.isNull
import com.dashaasavel.runapplib.grpc.error.UserRegistrationError
import com.dashaasavel.userservice.api.UserServiceGrpc
import com.dashaasavel.userservice.api.Userservice
import com.dashaasavel.userservice.api.Userservice.DeleteUser
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class UserServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var userServiceBlockingStub: UserServiceGrpc.UserServiceBlockingStub

    @Test
    fun `registration with the same username`() {
        val username = "test-user-${Random().nextInt()%5000}@gmail.com"
        val password = "password-${Random().nextInt()%5000}"

        val userId = registerUser(username, password)

        val user = getUserById(userId)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        assertGrpcCallThrows<StatusRuntimeException>(UserRegistrationError.USER_EXISTS_AND_CONFIRMED) {
            registerUser(username, password)
        }
    }

    @Test
    fun `registration and deletion user by id`() {
        val username = "test-user-${Random().nextInt()%5000}@gmail.com"
        val password = "password-${Random().nextInt()%5000}"

        val userId = registerUser(username, password)

        var user = getUserById(userId)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        deleteUserById(userId)

        user = getUserById(userId)

        assertTrue {
            user.isNull()
        }
    }

    @Test
    fun `registration and deletion user by username`() {
        val username = "test-user-${Random().nextInt()%5000}@gmail.com"
        val password = "password-${Random().nextInt()%5000}"

        val userId = registerUser(username, password)

        var user = getUserByUsername(username)

        assertEquals(userId, user.id)
        assertEquals(username, user.username)

        deleteUserByUsername(username)

        user = getUserByUsername(username)

        assertTrue {
            user.isNull()
        }
    }

    private fun registerUser(username: String, password: String): Int {
        val registerUserRequest = Userservice.RegisterUser.Request.newBuilder().apply {
            this.username = username
            this.password = password
        }.build()

        return userServiceBlockingStub.registerUser(registerUserRequest).userId
    }

    private fun getUserById(userId: Int): Userservice.User {
        val request = Userservice.GetUser.Request.newBuilder().apply {
            this.userId = userId
        }.build()
        return userServiceBlockingStub.getUser(request).user
    }

    private fun getUserByUsername(username: String): Userservice.User {
        val request = Userservice.GetUser.Request.newBuilder().apply {
            this.username = username
        }.build()
        return userServiceBlockingStub.getUser(request).user
    }

    private fun deleteUserById(userId: Int) {
        val request = DeleteUser.Request.newBuilder().apply {
            this.userId = userId
        }.build()
        userServiceBlockingStub.deleteUser(request)
    }

    private fun deleteUserByUsername(username: String) {
        val request = DeleteUser.Request.newBuilder().apply {
            this.username = username
        }.build()
        userServiceBlockingStub.deleteUser(request)
    }
}