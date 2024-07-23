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