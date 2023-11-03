package com.example.cookbook.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.cookbook.database.recipe.Recipe;
import com.example.cookbook.database.recipe.RecipeRepository;
import com.example.cookbook.network.Cuisine;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository recipeRepository;
    private LiveData<List<Recipe>> allRecipes;

    /**
     * Default, parameterless constructor.
     */
    public RecipeViewModel() {}

    /**
     * Construct and return a view model with an initialized data repository.
     * @param context The application context necessary to initialize the repository.
     * @see Context
     */
    public RecipeViewModel(Context context) {
        recipeRepository = new RecipeRepository(context);
        allRecipes = recipeRepository.getAll(1, Cuisine.AFRICAN);
    }

    /**
     * Initialize the view model's internal data repository.
     * @param context The application context necessary to initialize the repository.
     * @see Context
     */
    public void initRepository(Context context) {
        this.recipeRepository = new RecipeRepository(context);
        this.allRecipes = this.recipeRepository.getAll(1, Cuisine.AFRICAN);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
