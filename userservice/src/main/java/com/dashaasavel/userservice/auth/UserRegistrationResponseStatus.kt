package com.dashaasavel.userservice.auth

enum class UserRegistrationResponseStatus {
    USER_EXISTS_AND_CONFIRMED,
    NEED_TO_CONFIRM_ACCOUNT,
    NEW_TOKEN_WAS_SENT
}