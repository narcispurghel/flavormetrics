package com.flavormetrics.api.factory;

import com.flavormetrics.api.entity.Ingredient;
import com.flavormetrics.api.model.IngredientDto;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.IngredientRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IngredientFactory {
    private final IngredientRepository ingredientRepository;

    IngredientFactory(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public Set<Ingredient> checkIfExistsOrElseSave(AddRecipeRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("AddRecipeRequest cannot be null");
        }

        List<String> ingredientsNames = Optional.ofNullable(req.ingredients())
                .orElse(Collections.emptySet())
                .stream()
                .map(IngredientDto::name)
                .toList();

        List<IngredientDto> existingIngredients = ingredientRepository.getIdsAndNames(ingredientsNames);
        List<String> existingNames = existingIngredients.stream().map(IngredientDto::name).toList();

        List<Ingredient> newIngredients = new ArrayList<>();
        if (!existingIngredients.isEmpty()) {
            newIngredients = Optional.ofNullable(req.ingredients())
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(i -> !existingNames.contains(i.name()))
                    .map(Ingredient::new)
                    .toList();
        } else {
            newIngredients = ingredientsNames.stream()
                    .map(Ingredient::new)
                    .toList();
        }

        List<Ingredient> finalIngredients = existingIngredients
                .stream()
                .map(Ingredient::new)
                .collect(Collectors.toList());

        if (!newIngredients.isEmpty()) {
            List<Ingredient> saved = ingredientRepository.saveAll(newIngredients);
            finalIngredients.addAll(saved);
        }

        return Set.copyOf(finalIngredients);
    }
}
