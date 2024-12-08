package com.dashaasavel.authservice;

import org.springframework.http.HttpStatus;

public enum AccessTokenError implements CommonError {
    REFRESH_TOKEN_NOT_FOUND {
        @Override
        public String getName() {
            return this.name();
        }
    },
    REFRESH_TOKEN_EXPIRED {
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
