package com.dashaasavel.authservice;

import org.springframework.http.HttpStatus;

public interface CommonError {
    String getName();

    default HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
