package com.example.cookbook.database;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    final private RecipeRepository mRepository;

    private final LiveData<List<Recipe>> mAllRecipes;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAll();
    }

    LiveData<List<Recipe>> getAllRecipes() { return mAllRecipes; }

    public void insert(Recipe recipe) { mRepository.add(recipe); }
}
