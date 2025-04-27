package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.model.enums.RoleType;
import com.flavormetrics.api.repository.RegularUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RegularUserRepository regularUserRepository;

    public UserDetailsServiceImpl(RegularUserRepository regularUserRepository) {
        this.regularUserRepository = regularUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("username");
    }

    // TODO create admin, nutritionist logic
    public UserDetails loadUserByUsername(String username, RoleType role) {
        return switch (role) {
            case ROLE_ADMIN -> null;
            case ROLE_NUTRITIONIST -> null;
            case ROLE_USER -> regularUserRepository.findByUserDetails_Username_Value(username);
        };
    }
}
