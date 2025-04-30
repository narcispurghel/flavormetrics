package com.flavormetrics.api.entity.user.impl;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.model.enums.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nutritionist_user", schema = "users")
public class Nutritionist extends User {

    @OneToMany(mappedBy = "nutritionist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();

    public Nutritionist() {
        // Explicit no args constructor for JPA
    }

    private Nutritionist(Builder builder) {
        super(builder);
        this.recipes = builder.recipes;
    }

    public static class Builder extends User.Builder<Builder> {
        private List<Recipe> recipes;

        public Builder(String password, String username, RoleType role) {
            super(password, username, role);
        }

        public Builder receipes(List<Recipe> recipes) {
            this.recipes = recipes;
            return this;
        }

        public Nutritionist build() {
            return new Nutritionist(this);
        }
    }

    public List<Recipe> receipes() {
        return recipes;
    }
}
