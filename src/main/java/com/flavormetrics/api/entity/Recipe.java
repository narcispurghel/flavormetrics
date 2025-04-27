package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "receipe", schema = "food")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private Nutritionist nutritionist;

    @ManyToMany
    @JoinTable(
            name = "receipe_ingredient",
            joinColumns = {
                    @JoinColumn(name = "receipe_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ingredient_id")
            },
            schema = "food"
    )
    private List<Ingredient> ingredients;

    public Recipe() {
        // Explicit no args constructor for JPA
    }

    private Recipe(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.nutritionist = builder.nutritionist;
        this.ingredients = builder.ingredients;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private Nutritionist nutritionist;
        private List<Ingredient> ingredients = new ArrayList<>();

        public Builder(String name, List<Ingredient> ingredients) {

            if (name == null || name.isBlank()) {
                throw new InvalidArgumentException(
                        "Invalid name",
                        "name must be non-nul and non-blank",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "recipe.name"
                );
            }

            if (ingredients == null || ingredients.isEmpty()) {
                throw new InvalidArgumentException(
                        "Invalid ingredients",
                        "ingredients must be non-nul and non-empty",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "recipe.ingredients"
                );
            }

            this.name = name;
            this.ingredients = ingredients;
        }

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder nutritionist(Nutritionist nutritionist) {
            this.nutritionist = nutritionist;
            return this;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Nutritionist getNutritionist() {
        return nutritionist;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
