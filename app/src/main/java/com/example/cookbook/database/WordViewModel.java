package com.example.cookbook.database;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.cookbook.database.recipe.Recipe;
import com.example.cookbook.database.recipe.RecipeRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class WordViewModel extends AndroidViewModel {

    final private RecipeRepository mRepository;

    private final Single<List<Recipe>> mAllRecipes;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAllCached();
    }

    Single<List<Recipe>> getAllRecipes() { return mAllRecipes; }

    public void insert(Recipe recipe) { mRepository.add(recipe); }
}
