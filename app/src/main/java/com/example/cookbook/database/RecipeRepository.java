package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> recipes;

    RecipeRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.recipeDao = db.getRecipeDao();
        this.recipes = recipeDao.getAll();
    }

    void add(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            recipeDao.insert(recipe);
        });
    }

    LiveData<Recipe> getByUID(int uid) {
        return recipeDao.getByUID(uid);
    }

    LiveData<List<Recipe>> getAll() {
        return recipeDao.getAll();
    }

    void update(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            recipeDao.update(recipe);
        });
    }

    void delete(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            recipeDao.delete(recipe);
        });
    }
}
