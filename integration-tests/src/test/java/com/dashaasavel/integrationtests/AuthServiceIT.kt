package com.dashaasavel.integrationtests

import com.dashaasavel.integrationtests.utils.assertGrpcCallThrowsAuthException
import com.dashaasavel.runapplib.auth.AuthConstants
import com.dashaasavel.runapplib.auth.BearerToken
import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.userservice.api.UserServiceGrpc.UserServiceBlockingStub
import com.dashaasavel.userservice.api.Userservice
import io.grpc.CallCredentials
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.Executor

class AuthServiceIT : BaseServiceTest() {
    @Autowired
    private lateinit var userServiceBlockingStub: UserServiceBlockingStub

    @Test
    fun `call with no jwt token should throw an exception`() {
        val request = getUserRequest()

        assertGrpcCallThrowsAuthException<StatusRuntimeException>(AuthError.AUTH_TOKEN_IS_MISSING) {
            userServiceBlockingStub.getUser(request)

        }
    }

    @Test
    fun `call with unknown auth type should throw an exception`() {
        val request = getUserRequest()
        assertGrpcCallThrowsAuthException<StatusRuntimeException>(AuthError.UNKNOWN_AUTHORIZATION_TYPE) {
            userServiceBlockingStub.withCallCredentials(StrangeToken).getUser(request)

        }
    }

    @Test
    fun `call with strange jwt token should throw an exception`() {
        val request = getUserRequest()
        assertGrpcCallThrowsAuthException<StatusRuntimeException>(AuthError.UNKNOWN_ERROR) {
            userServiceBlockingStub.withCallCredentials(BearerToken("")).getUser(request)

        }
    }

    private fun getUserRequest(): Userservice.GetUser.Request {
        return Userservice.GetUser.Request.newBuilder().apply {
            this.userId = 1
        }.build()
    }

    private object StrangeToken : CallCredentials() {
        override fun applyRequestMetadata(
            requestInfo: RequestInfo,
            appExecutor: Executor,
            applier: MetadataApplier
        ) {
            appExecutor.execute {
                try {
                    val headers = Metadata()
                    headers.put(
                        AuthConstants.AUTHORIZATION_METADATA_KEY,
                        String.format("%s %s", "Beerer", "token")
                    )
                    applier.apply(headers)
                } catch (e: Exception) {
                    applier.fail(Status.UNAUTHENTICATED.withCause(e))
                }
            }
        }
    }
}