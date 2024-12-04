package com.dashaasavel.integrationtests

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthServiceIT : BaseServiceTest() {
    @Test
    fun `when user register with invalid token then error`() {
        val userId = authService.registerUser("dashaasavel", "password")
        assertNull(userId)
    }

    @Test
    fun `when refresh unknown access-token then error`() {
        val accessToken = authService.refreshAccessToken("unknown_refresh_token")
        assertNull(accessToken)
    }

    @Test
    fun `when user register with valid email then success`() {
        val userId = authService.registerUser()
        assertNotNull(userId)
    }

    @Test
    fun `when refresh existed token then success`() {
        val accessToken = authService.refreshAccessToken("48784c24-5aee-4798-9327-240545aa160f")
        assertNotNull(accessToken)
    }

    @Test
    fun `when refresh non-existed token then success`() {
        val accessToken = authService.refreshAccessToken("some-strange-token")
        assertNull(accessToken)
    }

    @Test
    fun `when auth existed user with correct credentials then success`() {
        val username = "dashaasavel3@gmail.com"
        val password = "password"
        // register
        val userId = authService.registerUser(username, password)
        assertNotNull(userId)
        val tokenPair = authService.authUser(username, password)
        assertNotNull(tokenPair)
    }

    @Test
    fun `when auth existed user with incorrect credentials then success`() {

    }

    @Test
    fun `when auth non-existed user then success()`() {

    }
}