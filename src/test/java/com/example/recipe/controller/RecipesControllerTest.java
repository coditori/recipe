package com.example.recipe.controller;

import com.example.recipe.controller.request.RecipeDto;
import com.example.recipe.search.QueryOperator;
import com.example.recipe.search.SearchFilter;
import com.example.recipe.search.SearchQuery;
import com.example.recipe.service.RecipeService;
import com.example.recipe.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = RecipeController.class)
class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @Test
    void should_return_201_when_valid_input_given() throws Exception {
        RecipeDto recipeDto = TestUtils.buildRecipeDto();

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_return_200_when_provided_recipe_id() throws Exception {
        mockMvc.perform(get("/recipes/{recipeId}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_200_when_hit_all_records() throws Exception {
        mockMvc.perform(get("/recipes")
                        .param("pageSize", "10")
                        .param("pageNumber", "0")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_200_when_update_record() throws Exception {
        RecipeDto recipeDto = TestUtils.buildRecipeDto();
        mockMvc.perform(get("/recipes/{recipeId}", 1)
                        .content(objectMapper.writeValueAsString(recipeDto))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_204_when_delete_record() throws Exception {
        mockMvc.perform(delete("/recipes/{recipeId}", 1)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_200_when_hit_search() throws Exception {
        SearchQuery searchQuery = SearchQuery.builder()
                .searchFilters(List.of(SearchFilter.builder()
                        .columnName("servings")
                        .operator(QueryOperator.EQUALS)
                        .value("4")
                        .build()))
                .build();

        mockMvc.perform(post("/recipes/q")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(searchQuery))
                )
                .andExpect(status().isOk());
    }
}