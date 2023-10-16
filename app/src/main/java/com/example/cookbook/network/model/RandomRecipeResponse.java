package com.example.cookbook.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomRecipeResponse {
    @SerializedName("recipes")
    private List<SpoonacularRecipe> recipeList;

    public List<SpoonacularRecipe> getRecipeList() {
        return this.recipeList;
    }
}
