package com.dashaasavel.authservice.auth

import com.dashaasavel.authservice.AccessTokenError
import com.dashaasavel.authservice.ErrorMessage
import com.dashaasavel.authservice.tokens.refresh.RefreshToken
import com.dashaasavel.authservice.tokens.refresh.RefreshTokenDAO
import com.dashaasavel.openapi.model.RefreshAccessTokenRequest
import com.dashaasavel.openapi.model.RefreshAccessTokenResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.sql.Timestamp
import java.util.*

private const val REFRESH_URL = "/refresh"

class RefreshTokenIT : BaseTest() {
    @Autowired
    lateinit var wac: WebApplicationContext

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var refreshTokenDAO: RefreshTokenDAO

    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun `when refresh token exists and not expired then issuing access token is success`() {
        val refreshToken = RefreshToken().apply {
            this.token = UUID.randomUUID().toString()
            this.userId = 1
            this.username = Fixtures.user.username
            this.expDate = Timestamp(System.currentTimeMillis() + 10_000)
        }
        refreshTokenDAO.saveToken(refreshToken)

        val request = RefreshAccessTokenRequest().apply {
            this.refreshToken = refreshToken.token
        }
        val responseBodyJson = mockMvc.perform(
            post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk).andReturn().response.contentAsString
        val response = objectMapper.readValue(responseBodyJson, RefreshAccessTokenResponse::class.java)

        assertThat(response.assessToken).isNotNull.isNotEmpty
    }

    @Test
    fun `when refresh token exists but is expired then issuing access token is failed`() {
        val refreshToken = RefreshToken().apply {
            this.token = UUID.randomUUID().toString()
            this.userId = 1
            this.username = Fixtures.user.username
            this.expDate = Timestamp(System.currentTimeMillis() - 10_000)
        }
        refreshTokenDAO.saveToken(refreshToken)

        val request = RefreshAccessTokenRequest().apply {
            this.refreshToken = refreshToken.token
        }
        val responseBodyJson = mockMvc.perform(
            post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest).andReturn().response.contentAsString

        val errorMessage = objectMapper.readValue(responseBodyJson, ErrorMessage::class.java)
        assertThat(errorMessage.message).isEqualTo(AccessTokenError.REFRESH_TOKEN_EXPIRED.name)
    }
}