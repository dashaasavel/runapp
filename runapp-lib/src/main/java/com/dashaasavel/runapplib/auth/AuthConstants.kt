package com.dashaasavel.runapplib.auth

import io.grpc.Context
import io.grpc.Metadata

object AuthConstants {
    const val BEARER_TYPE = "Bearer"
    val AUTHORIZATION_METADATA_KEY: Metadata.Key<String> = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
    @JvmField
    val AUTH_ERROR: Metadata.Key<String> = Metadata.Key.of("auth-error", Metadata.ASCII_STRING_MARSHALLER)
    val CLIENT_ID_CONTEXT_KEY: Context.Key<String> = Context.key("clientId")
    val JWT_KEY: Context.Key<String> = Context.key("token")
}