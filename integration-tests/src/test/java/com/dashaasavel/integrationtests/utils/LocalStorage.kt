package com.dashaasavel.integrationtests.utils

import com.dashaasavel.runapplib.auth.BearerToken
import io.grpc.Metadata

/**
 * Хранилище jwt-токенов для тестов
 */
class LocalStorage {
    private var authMetadata: Metadata? = null

    fun saveToken(jwtToken: String) {
        authMetadata = BearerToken.createMetadataWithBearerToken(jwtToken)
    }

    fun getAuthMetadata(): Metadata? = authMetadata
}