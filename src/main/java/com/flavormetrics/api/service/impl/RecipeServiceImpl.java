package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.*;
import com.flavormetrics.api.exception.ProfileNotFoundException;
import com.flavormetrics.api.exception.RecipeNotFoundException;
import com.flavormetrics.api.exception.UnAuthorizedException;
import com.flavormetrics.api.factory.AllergyFactory;
import com.flavormetrics.api.factory.IngredientFactory;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.factory.TagFactory;
import com.flavormetrics.api.mapper.RecipeMapper;
import com.flavormetrics.api.model.*;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import com.flavormetrics.api.service.RecipeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeFactory recipeFactory;
    private final IngredientFactory ingredientFactory;
    private final AllergyFactory allergyFactory;
    private final TagFactory tagFactory;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository,
                             RecipeFactory recipeFactory,
                             IngredientFactory ingredientFactory,
                             AllergyFactory allergyFactory,
                             TagFactory tagFactory) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.recipeFactory = recipeFactory;
        this.ingredientFactory = ingredientFactory;
        this.allergyFactory = allergyFactory;
        this.tagFactory = tagFactory;
    }

    @Override
    public UUID create(AddRecipeRequest req) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(req)
                .map(r -> recipeFactory.create(req, user.id()))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDto getById(UUID id) {
        return recipeRepository.getRecipeByIdEager(id)
                .map(RecipeDto::new)
                .orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    @Transactional
    public RecipeDto updateById(UUID id, AddRecipeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("AddRecipeRequest is null");
        }
        Recipe fromDb = recipeRepository.getRecipeByIdEager(id).orElseThrow(RecipeNotFoundException::new);
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = fromDb.getUser();
        if (owner == null || owner.getEmail() == null || owner.getEmail().getAddress() == null ||
            !owner.getEmail().getAddress().equals(principal)) {
            throw new UnAuthorizedException("You're not authorized to perform this operation");
        }
        Set<Ingredient> ingredients = Optional.of(request)
                .map(ingredientFactory::checkIfExistsOrElseSave)
                .orElse(Set.of());
        Set<Allergy> allergies = Optional.of(request)
                .map(AddRecipeRequest::allergies)
                .map(allergyFactory::checkIfExistsOrElseSave)
                .orElse(Set.of());
        Set<Tag> tags = Optional.of(request).map(tagFactory::checkIfExistsOrElseSave).orElse(Set.of());
        return Optional.of(request)
                .map(r -> {
                    fromDb.setIngredients(ingredients);
                    fromDb.setAllergies(allergies);
                    fromDb.setTags(tags);
                    fromDb.setInstructions(request.instructions());
                    fromDb.setDifficulty(request.difficulty());
                    fromDb.setUpdatedAt(LocalDateTime.now());
                    fromDb.setImageUrl(request.imageUrl());
                    fromDb.setCookTimeMinutes(request.cookTimeMinutes());
                    fromDb.setPrepTimeMinutes(request.prepTimeMinutes());
                    fromDb.setEstimatedCalories(request.estimatedCalories());
                    fromDb.setDietaryPreferences(request.dietaryPreferences());
                    return fromDb;
                })
                .map(recipeRepository::save)
                .map(RecipeDto::new)
                .get();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DataWithPagination<List<RecipeDto>> findAll(final int pageNumber, int pageSize) {
        // In Spring Data JPA first page is 0, and I think it's more convenient that the client to start from 1
        final int pageNumberMinusOne = pageNumber == 0 ? pageNumber : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumberMinusOne, pageSize);
        return Optional.of(recipeRepository.findAll(pageable))
                .map(p -> {
                    List<RecipeDto> dtos = p.getContent()
                            .stream()
                            .map(RecipeDto::new)
                            .toList();
                    return new DataWithPagination<>(dtos, p.getSize(), pageNumber, p.getTotalPages());
                })
                .get();
    }

    @Override
    @Transactional(readOnly = true)
    public DataWithPagination<RecipeByOwner> findAllByUserEmail(String email, final int pageNumber, int pageSize) {
        // In Spring Data JPA first page is 0, and I think it's more convenient that the client to start from 1
        final int pageNumberMinusOne = pageNumber == 0 ? pageNumber : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumberMinusOne, pageSize);
        return Optional.of(recipeRepository.findByOwner(email, pageable))
                .map(p -> new DataWithPagination<>(
                        RecipeMapper.toRecipeByOwner(p.getContent(), email), pageSize, pageNumber,
                        p.getTotalPages()))
                .get();
    }

    @Override
    @Transactional(readOnly = true)
    public DataWithPagination<List<RecipeDto>> findAllByRecipeFilter(
            RecipeFilter filter, final int pageNumber, int pageSize) {
        final int pageNumberMinusOne = pageNumber == 0 ? pageNumber : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumberMinusOne, pageSize);
        return Optional.of(recipeRepository.findAllByFilter(
                        filter.prepTimeMinutes(),
                        filter.cookTimeMinutes(),
                        filter.estimatedCalories(),
                        filter.difficulty(),
                        filter.dietaryPreference(),
                        pageable)
                )
                .map(p -> {
                    List<RecipeDto> dtos = p.getContent()
                            .stream()
                            .map(RecipeDto::new)
                            .toList();
                    return new DataWithPagination<>(dtos, p.getSize(), pageNumber, p.getTotalPages());
                })
                .get();
    }

    @Override
    @Transactional(readOnly = true)
    public DataWithPagination<Set<RecipeDto>> getRecommendations(final int pageNumber, int pageSize) {
        var principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // In Spring Data JPA first page is 0, and I think it's more convenient that the client to start from 1
        if (!userRepository.hasProfile(principal.id())) {
            throw new ProfileNotFoundException();
        }
        final int pageNumberMinusOne = pageNumber == 0 ? pageNumber : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumberMinusOne, pageSize);
        return Optional.of(recipeRepository.findAllRecommendations(principal.id(), pageable)
                )
                .map(p -> {
                    Set<RecipeDto> dtos = p.getContent()
                            .stream()
                            .map(RecipeDto::new)
                            .collect(Collectors.toSet());
                    return new DataWithPagination<>(dtos, p.getSize(), pageNumber, p.getTotalPages());
                }).get();
    }
}
