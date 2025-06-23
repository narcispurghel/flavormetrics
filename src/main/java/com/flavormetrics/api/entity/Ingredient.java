package com.flavormetrics.api.entity;

import com.flavormetrics.api.model.IngredientDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "updated_at", columnDefinition = "timestamp not null default current_timestamp")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp not null default current_timestamp")
    private final LocalDateTime createdAt;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Recipe> recipes;

    public Ingredient() {
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public Ingredient(IngredientDto dto) {
        this();
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("Ingredient DTO cannot be null");
        }
        this.id = dto.id();
        this.name = Objects.requireNonNullElse(dto.name(), "IngredientDto cannot be null");
    }

    public Ingredient(String name) {
        this();
        this.name = Objects.requireNonNullElse(name, "Ingredient name cannot be null");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Recipe> getRecipes() {
        return Set.copyOf(recipes);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = Set.copyOf(recipes);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ingredient that)) {
            return false;
        }
        return Objects.equals(name, that.name) &&
               Objects.equals(updatedAt, that.updatedAt) &&
               Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, updatedAt, createdAt);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        StringBuilder sb = new StringBuilder("Ingredient{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", recipes=").append(recipes.size());
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }

}
