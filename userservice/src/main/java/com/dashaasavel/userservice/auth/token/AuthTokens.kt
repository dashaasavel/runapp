package com.dashaasavel.userservice.auth.token

/**
 * Пара из access и refresh токенов, возвращающихся при аутентификации пользователя
 */
class AuthTokens(
    var accessToken: String,
    var refreshToken: String
)