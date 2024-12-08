package com.dashaasavel.runapplib.grpc.error

import org.springframework.http.HttpStatus

interface CommonError {
    fun getName(): String
    fun getStatus(): HttpStatus
}

enum class UserServiceError : CommonError {
    USER_ALREADY_EXISTS {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.CONFLICT
    }
}

enum class CreatingPlanError : CommonError {
    MORE_TIME_IS_NEEDED {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    COMPETITION_TYPE_NOT_SUPPORTED_NOW {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    NOT_SUPPORTED_COUNT_TIMES_A_WEEK {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    PLAN_ALREADY_EXISTS {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    USER_DOES_NOT_EXIST {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    }
}

enum class ChannelError : CommonError {
    CHANNEL_CANNOT_BE_NULL {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    },
    CHANNEL_IS_NOT_PERMITTED {
        override fun getName(): String = this.name
        override fun getStatus(): HttpStatus = HttpStatus.BAD_REQUEST
    }
}