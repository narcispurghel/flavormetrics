package com.flavormetrics.api.entity;

import com.flavormetrics.api.entity.user.User;
import com.flavormetrics.api.exception.impl.InvalidArgumentException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Entity
@Table(name = "rating", schema = "food")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Rating() {
        // No args constructor for JPA
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * Sets rating's value
     * @param value must be in the interval [0, 5]
     * @throws InvalidArgumentException in case the value 
     * is not in the range [0, 5]
     */
    public void setValue(Integer value) {
        if (value > 5 || value < 0) {
            throw new InvalidArgumentException(
                    "Invalid value",
                    "Rating's value must be in interval [0, 5]",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "rating.value");
        }
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}