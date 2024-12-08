package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.ErrorMessage
import com.dashaasavel.authservice.RegistrationError
import com.dashaasavel.authservice.api.UserServiceFacade
import com.dashaasavel.openapi.model.RegistrationRequest
import com.dashaasavel.openapi.model.RegistrationResponse
import com.dashaasavel.openapi.model.UserCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import io.grpc.Status
import io.grpc.StatusRuntimeException
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

private const val REGISTER_URL = "/register"

class RegistrationIT : BaseTest() {

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
    fun `when user is new then registration success`() {
        whenever(userService.saveUser(any(), any(), any())) doReturn 11
        val registrationRequest = RegistrationRequest().apply {
            this.firstName = "John"
            val uc = UserCredentials()
            uc.username = "john@gmail.com"
            uc.password = "john1234!"
            this.userCredentials = uc
        }
        val jsonRequest = objectMapper.writeValueAsString(registrationRequest)
        val responseBodyJson = mockMvc.perform(
            post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isOk).andReturn().response.contentAsString
        val response = objectMapper.readValue(responseBodyJson, RegistrationResponse::class.java)

        assertThat(response.userId).isEqualTo(11)
    }

    @Test
    fun `when user exists then registration fails`() {
        whenever(userService.saveUser(any(), any(), any())) doThrow StatusRuntimeException(Status.ALREADY_EXISTS)
        val registrationRequest = RegistrationRequest().apply {
            this.firstName = "John"
            val uc = UserCredentials()
            uc.username = "john@gmail.com"
            uc.password = "john1234!"
            this.userCredentials = uc
        }
        val jsonRequest = objectMapper.writeValueAsString(registrationRequest)
        val responseBodyJson = mockMvc.perform(
            post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isConflict).andReturn().response.contentAsString
        val errorMessage = objectMapper.readValue(responseBodyJson, ErrorMessage::class.java)
        assertThat(errorMessage.message).isEqualTo(RegistrationError.USER_EXISTS.name)
    }
}