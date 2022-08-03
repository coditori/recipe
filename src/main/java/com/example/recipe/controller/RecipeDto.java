package com.example.recipe.controller;

import com.example.recipe.model.DishType;
import com.example.recipe.model.IngredientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeDto {

    @Schema(description = "Recipe title", example = "Turkey")
    private String title;

    @Schema(description = "Is vegetarian or not", example = "REGULAR")
    private DishType dishType = DishType.REGULAR;

    @Schema(description = "Number of servings", example = "4")
    private Integer servings;

    @Schema(description = "Instructions for making this recipe",
            example = "Heat skillet to medium. Brown ground turkey in skillet, cooking until it is no longer pink " +
                    "Return turkey to skillet and stir in tomatoes, water, Italian seasoning, parsley, and oregano Bring to a boil")
    private String instructions;

    @Schema(description = "List of Ingredients", name = "ingredients")
    private List<IngredientDto> ingredients;
}
