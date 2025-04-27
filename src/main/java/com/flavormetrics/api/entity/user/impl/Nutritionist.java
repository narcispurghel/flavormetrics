package com.flavormetrics.api.entity.user.impl;

import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.user.User;
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
        this.recipes = builder.recipes;
    }

    public static class Builder {
        private List<Recipe> recipes;

        public Builder() {
            // TODO add argument
        }

        public Builder setReceipes(List<Recipe> recipes) {
            this.recipes = recipes;
            return this;
        }

        public Nutritionist build() {
            return new Nutritionist(this);
        }
    }

    public List<Recipe> getReceipes() {
        return recipes;
    }
}
