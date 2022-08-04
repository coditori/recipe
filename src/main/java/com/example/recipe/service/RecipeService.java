package com.example.recipe.service;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.exception.EntityNotFoundException;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.IngredientRepository;
import com.example.recipe.repository.RecipeRepository;
import com.example.recipe.search.SearchQuery;
import com.example.recipe.search.SpecificationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
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

    private void setRecipeForIngredients(Recipe recipeEntity) {
        for (Ingredient ingredient : recipeEntity.getIngredients()) ingredient.setRecipe(recipeEntity);
    }

    private Recipe findRecipeOrElseThrow(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Could not find recipe with id: " + recipeId));
    }

    @Transactional
    public Recipe create(RecipeDto recipeDto) {
        Recipe recipeEntity = getRecipeByDto(recipeDto);
        setRecipeForIngredients(recipeEntity);

        return recipeRepository.save(recipeEntity);
    }

    public Page<Recipe> getAll(int pageSize, int pageNumber) {
        return recipeRepository
                .findAll(PageRequest.of(pageNumber, pageSize == 0 ? 10 : pageSize));
    }

    public Recipe getById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("Could not find recipe with id: " + recipeId));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Recipe updateById(Long recipeId, RecipeDto recipeDto) {
        findRecipeOrElseThrow(recipeId);
        Recipe recipeEntity = getRecipeByDto(recipeDto);
        recipeEntity.setId(recipeId);
        setRecipeForIngredients(recipeEntity);
        ingredientRepository.deleteByRecipeId(recipeId);
        return recipeRepository.save(recipeEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRecipe(Long recipeId) {
        Recipe recipeEntity = findRecipeOrElseThrow(recipeId);
        recipeRepository.delete(recipeEntity);
    }

    public Page<Recipe> searchRecipes(SearchQuery searchQuery) {
        return recipeRepository.findAll(SpecificationUtil.bySearchQuery(searchQuery)
                , PageRequest.of(searchQuery.getPageNumber()
                        , searchQuery.getPageSize() == 0 ? 10 : searchQuery.getPageSize())
        );
    }
}
