package com.dashaasavel.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorMessage(
        @JsonProperty("message")
        String message
) {
}
