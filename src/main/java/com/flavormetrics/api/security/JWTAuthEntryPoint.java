package com.flavormetrics.api.security;

import com.flavormetrics.api.exception.impl.JWTAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        LOGGER.info("Caught auth exception: {}", e.getMessage());

        switch (e) {
            case JWTAuthenticationException j -> response.getWriter().write(j.getMessage());
            case UsernameNotFoundException u -> {

                response.getWriter().write(u.getMessage());
                response.setStatus(404);
            }
            case BadCredentialsException b -> {
                response.getWriter().write(b.getMessage());
                response.setStatus(404);
            }
            default -> response.getWriter().write(e.getMessage());
        }
    }
}