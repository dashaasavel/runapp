package com.dashaasavel.authservice;

import org.springframework.http.HttpStatus;

public enum AuthError implements CommonError {
    USER_DOES_NOT_EXIST {
        @Override
        public String getName() {
            return this.name();
        }
    },
    INCORRECT_PASSWORD {
        @Override
        public String getName() {
            return this.name();
        }
    },
    UNKNOWN_ERROR {
        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
