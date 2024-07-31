package com.dashaasavel.runapplib.auth

import io.grpc.CallCredentials
import io.grpc.Metadata
import io.grpc.Status
import java.util.concurrent.Executor

class BearerToken(
    private val jwtToken: String
) : CallCredentials() {
    companion object {
        fun createMetadataWithBearerToken(jwtToken: String): Metadata {
            val metadata = Metadata()
            metadata.put(
                AuthConstants.AUTHORIZATION_METADATA_KEY,
                String.format("%s %s", AuthConstants.BEARER_TYPE, jwtToken)
            )
            return metadata
        }
    }

    override fun applyRequestMetadata(
        requestInfo: RequestInfo,
        appExecutor: Executor,
        applier: MetadataApplier
    ) {
        appExecutor.execute {
            try {
                applier.apply(createMetadataWithBearerToken(jwtToken))
            } catch (e: Exception) {
                applier.fail(Status.UNAUTHENTICATED.withCause(e))
            }
        }
    }
}
