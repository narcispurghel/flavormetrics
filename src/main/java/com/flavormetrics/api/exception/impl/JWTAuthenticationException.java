package com.flavormetrics.api.exception.impl;

import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationException extends AuthenticationException {

    public JWTAuthenticationException(String msg) {
        super(msg);
    }
}