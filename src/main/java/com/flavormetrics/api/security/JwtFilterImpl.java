package com.flavormetrics.api.security;

import com.flavormetrics.api.config.SecurityConfig;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.exception.impl.JwtAuthenticationException;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JwtFilterImpl extends JwtFilter {
    private static final String[] PUBLIC_ENDPOINTS = SecurityConfig.getPublicEndpoints();
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilterImpl.class);

    public JwtFilterImpl(JwtService jwtService,
                         UserRepository userRepository,
                         JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            LOGGER.info("Processing request: {}", request.getRequestURI());
            final String path = request.getRequestURI();
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LOGGER.info("Getting authentication from SecurityContext");
            if (authentication != null) {
                LOGGER.info("Authentication is non-null, pass the filter");
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            LOGGER.info("Getting authorization header from request");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOGGER.info("Invalid authorization header: {}, throwing JWTAuthenticationException", authHeader);
                throw new JwtAuthenticationException("Missing or invalid authorization header");
            }
            final String JWT = authHeader.substring(7);
            if (!jwtService.isTokenValid(JWT)) {
                throw new JwtAuthenticationException("Missing or invalid JWT");
            }
            final String subject = jwtService.extractUsername(JWT);
            if (subject == null) {
                throw new JwtAuthenticationException("Missing or invalid username");
            }
            User user = userRepository.findByUsername_Value(subject)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Cannot find an account associated with username: " + subject));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            LOGGER.info("Caught auth exception: {}", e.getMessage());
            jwtAuthEntryPoint.commence(request, response, e);
        }
    }

    @Override
    protected boolean isPublicPath(String path) {
        final String publicPath;
        if (path.equals("/")) {
            publicPath = path;
        } else {
            publicPath = Arrays.stream(PUBLIC_ENDPOINTS)
                    .filter(e -> {
                        if (e.endsWith("/**")) {
                            String prefix = e.substring(0, e.length() - 3);
                            return path.startsWith(prefix);
                        } else {
                            return e.equals(path);
                        }
                    })
                    .collect(Collectors.joining());
        }
        return !publicPath.isEmpty();
    }
}