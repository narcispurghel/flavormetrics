package com.flavormetrics.api.service.impl;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.Recipe;
import com.flavormetrics.api.entity.User;
import com.flavormetrics.api.exception.RecipeNotFoundException;
import com.flavormetrics.api.exception.UnAuthorizedException;
import com.flavormetrics.api.factory.AllergyFactory;
import com.flavormetrics.api.factory.IngredientFactory;
import com.flavormetrics.api.factory.RecipeFactory;
import com.flavormetrics.api.factory.TagFactory;
import com.flavormetrics.api.mapper.RecipeMapper;
import com.flavormetrics.api.model.*;
import com.flavormetrics.api.model.enums.DietaryPreferenceType;
import com.flavormetrics.api.model.enums.DifficultyType;
import com.flavormetrics.api.model.request.AddRecipeRequest;
import com.flavormetrics.api.repository.RecipeRepository;
import com.flavormetrics.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    private UUID recipeId;
    private UUID userId;
    private Recipe recipe;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeFactory recipeFactory;

    @Mock
    private IngredientFactory ingredientFactory;

    @Mock
    private AllergyFactory allergyFactory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagFactory tagFactory;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Email email = new Email();
        email.setAddress("test@email.com");
        email.setUser(user);
        user.setEmail(email);
        recipe = new Recipe();
        recipe.setUser(user);
        recipe.setId(recipeId);
        var principal = new UserDetailsImpl(user);
        var auth = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void create_validRequest_returnsId() {
        AddRecipeRequest req = mock(AddRecipeRequest.class);
        UUID expectedId = UUID.randomUUID();
        when(recipeFactory.create(req, userId)).thenReturn(expectedId);
        UUID result = recipeService.create(req);
        assertEquals(expectedId, result);
        verify(recipeFactory).create(req, userId);
    }

    @Test
    void getById_existingRecipe_returnsDto() {
        RecipeDto dto = new RecipeDto(recipe);
        when(recipeRepository.getRecipeByIdEager(recipeId)).thenReturn(Optional.of(recipe));
        RecipeDto result = recipeService.getById(recipeId);
        assertEquals(dto, result);
    }

    @Test
    void getById_notFound_throwsException() {
        when(recipeRepository.getRecipeByIdEager(recipeId)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getById(recipeId));
    }

    @Test
    void updateById_authorized_updatesAndReturnsDto() {
        AddRecipeRequest req = mock(AddRecipeRequest.class);
        when(recipeRepository.getRecipeByIdEager(recipeId)).thenReturn(Optional.of(recipe));
        when(ingredientFactory.checkIfExistsOrElseSave(req)).thenReturn(Set.of());
        when(allergyFactory.checkIfExistsOrElseSave(any())).thenReturn(Set.of());
        when(tagFactory.checkIfExistsOrElseSave(req)).thenReturn(Set.of());
        when(recipeRepository.save(any())).thenReturn(recipe);
        RecipeDto result = recipeService.updateById(recipeId, req);
        assertEquals(new RecipeDto(recipe), result);
    }

    @Test
    void updateById_unauthorized_throwsException() {
        AddRecipeRequest req = mock(AddRecipeRequest.class);
        Recipe recipe = new Recipe();
        User otherUser = new User();
        Email email = new Email();
        email.setAddress("other@example.com");
        otherUser.setEmail(email);
        recipe.setUser(otherUser);
        when(recipeRepository.getRecipeByIdEager(recipeId)).thenReturn(Optional.of(recipe));
        assertThrows(UnAuthorizedException.class, () -> recipeService.updateById(recipeId, req));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        recipeService.deleteById(recipeId);
        verify(recipeRepository).deleteById(recipeId);
    }

    @Test
    void findAll_returnsPaginatedData() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Recipe> recipes = List.of(recipe);
        when(recipeRepository.findAll(pageable)).thenReturn(new PageImpl<>(recipes));
        DataWithPagination<List<RecipeDto>> result = recipeService.findAll(1, 10);
        assertEquals(1, result.data().size());
    }

    @Test
    void findAllByUserEmail_returnsPaginatedOwnerData() {
        try (MockedStatic<RecipeMapper> mockedRecipeMapper = mockStatic(RecipeMapper.class)) {
            Pageable pageable = PageRequest.of(0, 10);
            String email = "test@example.com";
            List<Recipe> recipes = List.of(recipe);
            List<RecipeDto> recipesDto = recipes.stream().map(RecipeDto::new).toList();
            when(recipeRepository.findByOwner(email, pageable)).thenReturn(new PageImpl<>(recipes));
            when(RecipeMapper.toRecipeByOwner(any(), eq(email))).thenReturn(new RecipeByOwner(email, recipesDto));
            DataWithPagination<RecipeByOwner> result = recipeService.findAllByUserEmail(email, 1, 10);
            assertEquals(1, result.data().recipes().size());
            assertEquals(email, result.data().owner());
        }
    }

    @Test
    void findAllByRecipeFilter_returnsFilteredPaginatedData() {
        Pageable pageable = PageRequest.of(0, 10);
        RecipeFilter filter = new RecipeFilter(10, 10, 200, DifficultyType.EASY, DietaryPreferenceType.VEGAN);
        List<Recipe> recipes = List.of(recipe);
        when(recipeRepository.findAllByFilter(10, 10, 200, DifficultyType.EASY, DietaryPreferenceType.VEGAN, pageable))
                .thenReturn(new PageImpl<>(recipes));
        DataWithPagination<List<RecipeDto>> result = recipeService.findAllByRecipeFilter(filter, 1, 10);
        assertEquals(1, result.data().size());
    }

    @Test
    void getRecommendations_returnsPaginatedData() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Recipe> recipes = List.of(recipe);
        when(userRepository.hasProfile(any())).thenReturn(true);
        when(recipeRepository.findAllRecommendations(userId, pageable)).thenReturn(new PageImpl<>(recipes));
        DataWithPagination<Set<RecipeDto>> result = recipeService.getRecommendations(0, 10);
        assertEquals(1, result.data().size());
    }
}