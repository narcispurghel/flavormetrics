package com.flavormetrics.api.entity.user.impl;

import java.util.ArrayList;
import java.util.List;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "nutritionist_user", schema = "users")
public class Nutritionist extends User {
    @OneToMany(mappedBy = "nutritionist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();

    public Nutritionist() {
        // No args constructor for JPA
    }

    public Nutritionist(String password, Email username) {
        super(password, username);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
