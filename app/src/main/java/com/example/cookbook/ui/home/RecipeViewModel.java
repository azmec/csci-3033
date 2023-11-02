package com.example.cookbook.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.cookbook.database.recipe.Recipe;
import com.example.cookbook.database.recipe.RecipeRepository;
import com.example.cookbook.network.Cuisine;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final LiveData<List<Recipe>> allRecipes;

    public RecipeViewModel(Context context) {
        recipeRepository = new RecipeRepository(context);
        allRecipes = recipeRepository.getAll(1, Cuisine.AFRICAN);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
