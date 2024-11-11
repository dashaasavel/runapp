package com.dashaasavel.grpcmessages.utils;

import com.dashaasavel.userservice.api.Authservice;

public class AuthServiceMessageWrapper {
    public static Authservice.UserCredentials userCredentials(String username, String password) {
        return Authservice.UserCredentials.newBuilder().setUsername(username).setPassword(password).build();
    }
}
