package com.dashaasavel.authservice;

import org.springframework.http.HttpStatus;

public enum RegistrationError implements CommonError {
    USER_EXISTS {
        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.CONFLICT;
        }
    },
    INVALID_EMAIL {
        @Override
        public String getName() {
            return this.name();
        }
    },
    INTERNAL {
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
