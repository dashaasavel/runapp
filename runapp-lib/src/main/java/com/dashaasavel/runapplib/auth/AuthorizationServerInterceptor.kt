package com.dashaasavel.runapplib.auth

import com.dashaasavel.runapplib.grpc.error.AuthError
import com.dashaasavel.runapplib.grpc.error.GrpcMetadataUtils
import com.dashaasavel.runapplib.grpc.getServiceAndMethodName
import com.dashaasavel.runapplib.logger
import io.grpc.*
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import java.nio.charset.StandardCharsets
import java.sql.Date

/**
 * Валидация Jwt-токена пользователя
 */
class AuthorizationServerInterceptor : ServerInterceptor, InitializingBean {
    private val logger = logger()

    @Value("\${jwt.signing-key}")
    var signingKey: String = ""

    private val bearerTypeLength = AuthConstants.BEARER_TYPE.length
    private lateinit var parser: JwtParser

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        if (call.getServiceAndMethodName().contains("AuthService")) {
            return next.startCall(
                object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {}, headers
            )
        }
        val value = headers[AuthConstants.AUTHORIZATION_METADATA_KEY]
        if (value == null) {
            println("${call.getServiceAndMethodName()} token is missing!!!")
            headers.merge(GrpcMetadataUtils.invalidClientAuthData(AuthError.AUTH_TOKEN_IS_MISSING))
        } else if (!value.startsWith(AuthConstants.BEARER_TYPE)) {
            headers.merge(GrpcMetadataUtils.invalidClientAuthData(AuthError.UNKNOWN_AUTHORIZATION_TYPE))
        } else {
            try {
                val token = value.substring(bearerTypeLength).trim()

                val claims = parser.parseSignedClaims(token)
                val userId = claims.payload.subject
                val expiration = claims.payload.expiration
                if (Date(System.currentTimeMillis()).after(expiration)) {
                    headers.merge(GrpcMetadataUtils.invalidClientAuthData(AuthError.TOKEN_IS_OUT_OF_DATE))
                } else {
                    val ctx = Context.current()
                        .withValue(AuthConstants.JWT_KEY, token)
                        .withValue(AuthConstants.CLIENT_ID_CONTEXT_KEY, userId)
                    return Contexts.interceptCall(ctx, call, headers, next)
                }
            } catch (e: Exception) {
                headers.merge(GrpcMetadataUtils.invalidClientAuthData(AuthError.UNKNOWN_ERROR))
                logger.error("Error while parsing JWT-token", e)
            }
        }
        call.close(Status.UNAUTHENTICATED, headers)
        return object : ServerCall.Listener<ReqT>() { // noop
        }
    }

    override fun afterPropertiesSet() {
        val signingKey = Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))

        parser = Jwts.parser().setSigningKey(signingKey).build()
        parser = Jwts.parser().verifyWith(signingKey).build()
    }
}