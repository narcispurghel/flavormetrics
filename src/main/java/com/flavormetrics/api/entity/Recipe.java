package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @ManyToMany(cascade = CascadeType.ALL)
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
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
        this.ingredients = new ArrayList<>();
    }

    public Recipe(String name, List<Ingredient> ingredients) {
        if (name == null || name.isBlank()) {
            throw new InvalidArgumentException(
                    "Invalid name",
                    "name must be non-null and non-blank",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "recipe.name"
            );
        }

        if (ingredients == null || ingredients.isEmpty()) {
            throw new InvalidArgumentException(
                    "Invalid ingredients",
                    "ingredients must be non-null and non-empty",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "recipe.ingredients"
            );
        }

        this.name = name;
        this.ingredients = ingredients;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Nutritionist getNutritionist() {
        return nutritionist;
    }

    public void setNutritionist(Nutritionist nutritionist) {
        this.nutritionist = nutritionist;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Recipe recipe)) return false;
        return Objects.equals(name, recipe.name) && Objects.equals(ingredients, recipe.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients);
    }
}
