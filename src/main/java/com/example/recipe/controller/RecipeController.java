package com.example.recipe.controller;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.model.Recipe;
import com.example.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "recipes")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    @Operation(
            summary = "Create a new recipe",
            tags = {"recipes"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "New recipe created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid input/object is sent", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
                    })
            }
    )
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto) {
        return new ResponseEntity<>(recipeService.createRecipe(recipeDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            operationId = "getAllRecipes",
            summary = "get All Recipes",
            tags = {"recipes"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns recipes list")
            }
    )
    public ResponseEntity<Page<Recipe>> getAllRecipes(
            @Parameter(name = "pageSize", example = "10") @RequestParam(required = false) Integer pageSize,
            @Parameter(name = "pageNumber", example = "0") @RequestParam(required = false) Integer pageNumber) {
        return new ResponseEntity<>(recipeService.getAllRecipes(pageSize, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/{recipeId}")
    @Operation(
            operationId = "getRecipe",
            summary = "get existed Recipe",
            tags = {"recipes"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return only one recipe", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))
                    })
            }
    )
    public ResponseEntity<Recipe> getRecipe(
            @Parameter(name = "recipeId", example = "1", description = "recipe id", required = true) @PathVariable("recipeId") Long recipeId) {
        return new ResponseEntity<>(recipeService.getRecipeById(recipeId), HttpStatus.OK);
    }
}