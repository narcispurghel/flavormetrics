package com.flavormetrics.api.entity.user.impl;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends User {

    public Admin(String password, Email email) {
        super(password, email);
    }

    public Admin() {
        // No args constructor for JPA
    }
}