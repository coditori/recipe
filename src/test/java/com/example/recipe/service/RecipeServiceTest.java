package com.example.recipe.service;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.exception.EntityNotFoundException;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.IngredientRepository;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.search.QueryOperator;
import com.example.recipe.search.SearchFilter;
import com.example.recipe.search.SearchQuery;
import com.example.recipe.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientRepository ingredientsRepository;

    private ObjectMapper objectMapper;

    private RecipeService recipeService;

    private Recipe recipeEntity;
    private RecipeDto recipeDto;
    private Long recipeId = 20L;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.buildObjectMapper();
        recipeDto = TestUtils.buildRecipeDto();
        recipeEntity = objectMapper.convertValue(recipeDto, Recipe.class);
        recipeEntity.setId(recipeId);

        recipeService = new RecipeService(recipeRepository, ingredientsRepository, objectMapper);
    }

    @Test
    void testCreateRecipe() {
        Mockito.when(recipeRepository.save(ArgumentMatchers.any(Recipe.class))).thenReturn(recipeEntity);

        Recipe recipeDto = recipeService.create(TestUtils.buildRecipeDto());
        assertTrue(recipeDto.getId() > 0);
    }

    @Test
    void testGetRecipe() {
        Mockito.when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(recipeEntity));

        Recipe fetchedRecipe = recipeService.getById(recipeId);
        assertEquals(fetchedRecipe.getId(), recipeId);
    }

    @Test
    void testGetRecipeThrownExceptionForNonExistingEntity() {
        Mockito.when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> recipeService.getById(recipeId));
        assertTrue(exception.getMessage().contains(recipeId + ""));
    }

    @Test
    void testUpdateRecipe() {
        String newTitle = "Mystical Author";
        recipeEntity.setTitle(newTitle);
        Mockito.when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(recipeEntity));
        Mockito.when(recipeRepository.save(ArgumentMatchers.any(Recipe.class))).thenReturn(recipeEntity);

        Recipe updatedRecipe = recipeService.updateById(recipeId, recipeDto);
        assertEquals(updatedRecipe.getTitle(), newTitle);
    }

    @Test
    void testDeleteRecipe() {
        Mockito.when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(recipeEntity));
        recipeService.deleteRecipe(recipeId);
        Mockito.when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> recipeService.getById(recipeId));
        assertTrue(exception.getMessage().contains(recipeId + ""));
    }

    @Test
    void testSearch() {
        Mockito.when(recipeRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recipeEntity)));
        SearchQuery searchQuery = SearchQuery.builder()
                .pageSize(10)
                .pageNumber(0)
                .searchFilters(List.of(SearchFilter.builder()
                        .columnName("title")
                        .operator(QueryOperator.IN)
                        .value(List.of(recipeEntity.getTitle()))
                        .build()))
                .build();
        Page<Recipe> recipeDtoList = recipeService.searchRecipes(searchQuery);
        assertTrue(recipeDtoList.getSize() >= 1);
    }

    @Test
    void testToEntity() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setServings(5);
        Method method = RecipeService.class.getDeclaredMethod("getRecipeByDto", RecipeDto.class);
        method.setAccessible(true);
        Recipe recipe = (Recipe) method.invoke(recipeService, recipeDto);
        assertNotNull(recipeDto);
        assertEquals(recipeDto.getServings(), recipe.getServings());
    }
}