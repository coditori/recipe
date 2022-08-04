package com.example.recipe.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDto {

    @Schema(description = "A component of the recipe", example = "potato")
    private String ingredient;

    @Schema(description = "Amount of ingredient", example = "2")
    private String amount;
}