package com.flavormetrics.api.entity;

import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ingredient", schema = "food")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipes;

    public Ingredient() {
        // Explicit no args constructor for JPA
    }

    private Ingredient(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.recipes = builder.recipes;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private List<Recipe> recipes;

        public Builder(String name) {

            if (name == null || name.isBlank()) {
                throw new InvalidArgumentException(
                        "Invalid name",
                        "name must be non-nul and non-blank",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "recipe.name"
                );
            }

            this.name = name;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public void setRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
        }

        public Ingredient build() {
            return new Ingredient(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
