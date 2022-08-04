package com.example.recipe.service;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.exception.EntityNotFoundException;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.search.QueryOperator;
import com.example.recipe.search.SearchFilter;
import com.example.recipe.search.SearchQuery;
import com.example.recipe.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void should_create_recipe() {
        Recipe recipeDto = recipeService.create(TestUtils.buildRecipeDto());
        assertTrue(recipeDto.getId() > 0);
    }

    @Test
    void should_return_existing_recipe() {
        recipeRepository.findAll(PageRequest.of(0, 1)).get().findFirst()
                .ifPresent(recipe -> {
                    Recipe fetchedRecipe = recipeService.getById(recipe.getId());
                    assertEquals(fetchedRecipe.getId(), recipe.getId());
                });
    }

    @Test
    void should_throw_exception_non_existing_recipe() {
        Long entityId = -1L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> recipeService.getById(entityId));
        assertTrue(exception.getMessage().contains(entityId + ""));
    }

    @Test
    void should_update_existing_recipe() {
        recipeRepository.findAll(PageRequest.of(0, 1)).get().findFirst()
                .ifPresent(recipe -> {
                    RecipeDto recipeDto = TestUtils.buildRecipeDto();
                    recipeDto.setTitle(recipeDto.getTitle() + "__CHANGED");
                    Recipe updatedRecipe = recipeService.updateById(recipe.getId(), recipeDto);
                    assertEquals(updatedRecipe.getTitle(), recipeDto.getTitle());
                });
    }

    @Test
    void should_delete_existing_recipe() {
        recipeRepository.findAll(PageRequest.of(0, 1)).get().findFirst()
                .ifPresent(recipe -> {
                    recipeService.deleteRecipe(recipe.getId());
                    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> recipeService.getById(recipe.getId()));
                    assertTrue(exception.getMessage().contains(recipe.getId() + ""));
                });
    }

    @Test
    void should_throw_exception_when_delete_non_existing_recipe() {
        Long entityId = -1L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(entityId));
        assertTrue(exception.getMessage().contains(entityId + ""));
    }

    @Test
    void should_return_at_least_one_record_with_existing_column_value() {
        recipeRepository.findAll(PageRequest.of(0, 1)).get().findFirst()
                .ifPresent(recipe -> {
                    SearchQuery searchQuery = SearchQuery.builder()
                            .searchFilters(List.of(SearchFilter.builder()
                                    .columnName("title")
                                    .operator(QueryOperator.IN)
                                    .value(List.of(recipe.getTitle()))
                                    .build()))
                            .build();
                    Page<Recipe> recipeDtoList = recipeService.searchRecipes(searchQuery);
                    assertTrue(recipeDtoList.getSize() >= 1);
                });
    }
}