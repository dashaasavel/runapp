package com.dashaasavel.runapplib.grpc.error

/**
 * Маркерный интерфейс для ошибок, которые будут переданы клиенту при grpc-вызове
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
    },
    NEED_TO_CONFIRM_THE_LATEST_TOKEN {
        override fun getName(): String = this.name
    },
    INVALID_EMAIL {
        override fun getName(): String = this.name
    },
    TOKEN_NOT_FOUND {
        override fun getName(): String = this.name
    }
}

enum class UserAuthError: CommonError {
    USER_DOES_NOT_EXIST {
        override fun getName(): String = this.name
    },
    INCORRECT_PASSWORD {
        override fun getName(): String = this.name
    }
}

enum class AuthError: CommonError {
    AUTH_TOKEN_IS_MISSING {
        override fun getName(): String = this.name
    },
    UNKNOWN_AUTHORIZATION_TYPE {
        override fun getName(): String = this.name
    },
    TOKEN_IS_OUT_OF_DATE {
        override fun getName(): String = this.name
    },
    UNKNOWN_ERROR {
        override fun getName(): String = this.name
    }
}

enum class CreatingPlanError: CommonError {
    MORE_TIME_IS_NEEDED {
        override fun getName(): String = this.name
    },
    COMPETITION_TYPE_NOT_SUPPORTED_NOW {
        override fun getName(): String = this.name
    },
    NOT_SUPPORTED_COUNT_TIMES_A_WEEK {
        override fun getName(): String = this.name
    },
    PLAN_ALREADY_EXISTS {
        override fun getName(): String = this.name
    },
    USER_DOES_NOT_EXIST {
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