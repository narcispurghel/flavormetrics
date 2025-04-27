package com.flavormetrics.api.config;

import com.flavormetrics.api.service.JwtService;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] publicApiEndpoints = {
            "/test",
            "/static/**",
            "/",
            "/favicon.ico",
            "/api/auth/register",
            "/api/auth/login",
    };

    private final String[] swaggerEndpoints = {
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
                .oauth2ResourceServer(oAuthConfig ->
                        oAuthConfig.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtService.getSecretKey()).build();
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

    private String[] getPublicEndpoints() {
        List<String> publicEndpointsTemp = new ArrayList<>();
        publicEndpointsTemp.addAll(List.of(swaggerEndpoints));
        publicEndpointsTemp.addAll(List.of(publicApiEndpoints));
        String[] publicEndpoints = new String[publicEndpointsTemp.size()];
        for (int i = 0; i < publicEndpointsTemp.size(); ++i) {
            publicEndpoints[i] = publicEndpointsTemp.get(i);
        }
        logger.debug("publicEndpoints: {}", (Object) publicEndpoints);
        return publicEndpoints;
    }
}
