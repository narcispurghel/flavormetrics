package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity
@Table(name = "authority", schema = "users")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Authority() {
        // Explicit no args constructor for JPA
    }

    public Authority(RoleType role) {
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public RoleType getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    public User getUser() {
        return user;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
