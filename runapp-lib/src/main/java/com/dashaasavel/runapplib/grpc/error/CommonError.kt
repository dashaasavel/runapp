package com.dashaasavel.runapplib.grpc.error

/**
 * Маркерный интерфейс для ошибок, передающихся прCommonErrorи grpc-вызовах
 */
interface CommonError {
    fun getName(): String
}

enum class UserRegistrationError : CommonError {
    USER_EXISTS_AND_CONFIRMED {
        override fun getName(): String = this.name
    },
    NEED_TO_CONFIRM_ACCOUNT {
        override fun getName(): String = this.name
    },
    NEW_TOKEN_WAS_SENT {
        override fun getName(): String = this.name
    }
}

enum class ChannelError : CommonError {
    CHANNEL_CANNOT_BE_NULL {
        override fun getName(): String = this.name
    },
    CHANNEL_IS_NOT_PERMITTED {
        override fun getName(): String = this.name
    }
}