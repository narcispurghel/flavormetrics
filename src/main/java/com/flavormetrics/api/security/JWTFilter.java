package com.flavormetrics.api.security;

import com.flavormetrics.api.config.SecurityConfig;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.exception.impl.JWTAuthenticationException;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private static final String[] PUBLIC_ENDPOINTS = SecurityConfig.getPublicEndpoints();
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    public JWTFilter(JWTService jwtService, UserRepository userRepository, JWTAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String path = request.getRequestURI();
            LOGGER.info("Processing request: {}", request.getRequestURI());
            String publicPath = Arrays.stream(PUBLIC_ENDPOINTS)
                    .filter(e -> e.contains(path))
                    .collect(Collectors.joining());
            if (!publicPath.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LOGGER.info("Getting authentication from SecurityContext: {}", authentication);
            if (authentication != null) {
                LOGGER.info("Authentication is non-null, pass the filter");
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            LOGGER.info("Getting authorization header from request");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOGGER.info("Invalid authorization header: {}, throwing JWTAuthenticationException", authHeader);
                //return;
                throw new JWTAuthenticationException("Missing or invalid authorization header");
            }
            final String JWT = authHeader.substring(7);
            if (!jwtService.isTokenValid(JWT)) {
                throw new JWTAuthenticationException("Missing or invalid JWT");
            }
            final String subject = jwtService.extractUsername(JWT);
            if (subject == null) {
                throw new JWTAuthenticationException("Missing or invalid username");
            }
            User user = userRepository.findByUsername_Value(subject)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Cannot find an account associated with username: " + subject));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (JWTAuthenticationException e) {
            LOGGER.info("Caught auth exception: {}", e.getMessage());
            jwtAuthEntryPoint.commence(request, response, e);
        }
    }

}