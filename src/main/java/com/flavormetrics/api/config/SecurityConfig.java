package com.flavormetrics.api.config;

import com.flavormetrics.api.security.CustomAccessDeniedHandler;
import com.flavormetrics.api.security.JWTAuthEntryPoint;
import com.flavormetrics.api.security.JWTFilter;
import com.flavormetrics.api.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private static final String[] publicApiEndpoints = {
            "/test",
            "/static/**",
            "/",
            "/favicon.ico",
            "/api/auth/register",
            "/api/auth/login",
    };

    private static final String[] swaggerEndpoints = {
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/webjars/**"
    };

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JWTFilter jwtFilter;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JWTService jwtService,
                          UserDetailsService userDetailsService,
                          JWTFilter jwtFilter,
                          JWTAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(getPublicEndpoints())
                            .permitAll();
                    request.requestMatchers("/api/test/auth")
                            .hasAnyRole("ADMIN", "USER", "NUTRITIONIST");
                    request.anyRequest().authenticated();
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager())
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint(jwtAuthEntryPoint);
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    public static String[] getPublicEndpoints() {
        List<String> publicEndpointsTemp = new ArrayList<>();
        publicEndpointsTemp.addAll(List.of(swaggerEndpoints));
        publicEndpointsTemp.addAll(List.of(publicApiEndpoints));
        String[] publicEndpoints = new String[publicEndpointsTemp.size()];
        for (int i = 0; i < publicEndpointsTemp.size(); ++i) {
            publicEndpoints[i] = publicEndpointsTemp.get(i);
        }
        return publicEndpoints;
    }
}
