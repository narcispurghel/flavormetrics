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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String[] PUBLIC_ENDPOINTS = SecurityConfig.getPublicEndpoints();
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtService jwtService,
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
            final String path = request.getRequestURI();
            final String publicPath;
            LOGGER.info("Processing request: {}", request.getRequestURI());
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
                    user.getPassword(),
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("Checking authentication");
            boolean isAuth = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
            if (isAuth) {
                logger.info("Authentication success!");
            } else {
                logger.info("Authentication failed!");
            }
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            LOGGER.info("Caught auth exception: {}", e.getMessage());
            jwtAuthEntryPoint.commence(request, response, e);
        }
    }

}