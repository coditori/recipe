package com.example.recipe.service;

import com.example.recipe.controller.RecipeDto;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.IngredientRepository;
import com.example.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipeEntity = getRecipe(recipeDto);
        for (Ingredient ingredient : recipeEntity.getIngredients()) {
            ingredient.setRecipe(recipeEntity);
        }
        return recipeRepository.save(recipeEntity);
    }

    private Recipe getRecipe(RecipeDto recipeDto) {
        return objectMapper.convertValue(recipeDto, Recipe.class);
    }
}
