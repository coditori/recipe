package com.example.recipe.util;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.exception.ApiException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    public static ObjectMapper buildObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static String RECIPE_JSON = "{\n" +
            "  \"title\": \"Pesto Pizza\",\n" +
            "  \"dishType\": \"REGULAR\",\n" +
            "  \"servings\": 4,\n" +
            "  \"instructions\": \"Preheat oven to 450 degrees F (230 degrees C).Spread pesto on pizza crust. Top with tomatoes, bell peppers, olives, red onions, artichoke hearts and feta cheese. Bake for 8 to 10 minutes, or until cheese is melted and browned.\",\n" +
            "  \"ingredients\": [\n" +
            "    {\n" +
            "      \"ingredient\": \"crumbled feta cheese\",\n" +
            "      \"amount\": \"1 cup\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static RecipeDto buildRecipeDto(){
        try {
            return buildObjectMapper().readValue(RECIPE_JSON,RecipeDto.class);
        }catch (Exception exception){
            throw new ApiException("Error when trying to build recipeDto from JSON string");
        }
    }
}
