package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.AuthError
import com.dashaasavel.authservice.ErrorMessage
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.openapi.model.AuthRequest
import com.dashaasavel.openapi.model.AuthResponse
import com.dashaasavel.openapi.model.UserCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

private const val AUTH_URL = "/auth"

class AuthIT : BaseTest() {

    @Autowired
    lateinit var wac: WebApplicationContext

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var userService: UserServiceFacade

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun `when user auth with valid username and password then auth success`() {
        // юзер существует, аутентифицируемся с правильным логином и паролем, все ок
        whenever(userService.getUser(any())) doReturn Fixtures.userFromDB

        val authRequest = AuthRequest().apply {
            val uc = UserCredentials()
            uc.username = Fixtures.user.username
            uc.password = Fixtures.user.password
            this.userCredentials = uc
        }
        val jsonRequest = objectMapper.writeValueAsString(authRequest)
        val responseBodyJson = mockMvc.perform(
            post(AUTH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isOk).andReturn().response.contentAsString

        val authResponse = objectMapper.readValue(responseBodyJson, AuthResponse::class.java)
        assertThat(authResponse.accessToken).isNotEmpty
        assertThat(authResponse.refreshToken).isNotEmpty
    }

    @Test
    fun `when user auth with invalid password then auth failed`() {
        // юзер существует, аутентифицируемся с неправильным логином и/или паролем, падаем
        whenever(userService.getUser(any())) doReturn Fixtures.userFromDB

        val authRequest = AuthRequest().apply {
            val uc = UserCredentials()
            uc.username = Fixtures.user.username
            uc.password = Fixtures.user.password + "1"
            this.userCredentials = uc
        }
        val jsonRequest = objectMapper.writeValueAsString(authRequest)
        val responseBodyJson = mockMvc.perform(
            post(AUTH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isBadRequest).andReturn().response.contentAsString

        val errorMessage = objectMapper.readValue(responseBodyJson, ErrorMessage::class.java)
        assertThat(errorMessage.message).isEqualTo(AuthError.INCORRECT_PASSWORD.name)
    }
}