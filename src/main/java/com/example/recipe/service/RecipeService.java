package com.example.recipe.service;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.exception.EntityNotFoundException;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.IngredientRepository;
import com.example.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Recipe getRecipeByDto(RecipeDto recipeDto) {
        return objectMapper.convertValue(recipeDto, Recipe.class);
    }

    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipeEntity = getRecipeByDto(recipeDto);
        for (Ingredient ingredient : recipeEntity.getIngredients()) {
            ingredient.setRecipe(recipeEntity);
        }
        return recipeRepository.save(recipeEntity);
    }

    public Page<Recipe> getAllRecipes(int pageSize, int pageNumber) {
        return recipeRepository
                .findAll(PageRequest.of(pageNumber, pageSize == 0 ? 10 : pageSize));
    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Could not find recipe with id: " + recipeId));
    }
}
