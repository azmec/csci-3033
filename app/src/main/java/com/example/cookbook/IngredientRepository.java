package com.example.cookbook;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientRepository {
    private IngredientDao ingredientDao;
    private LiveData<List<Ingredient>> ingredients;

    IngredientRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.ingredientDao = db.getIngredientDao();
        this.ingredients = ingredientDao.getAll();
    }

    void add(Ingredient ingredient) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            ingredientDao.insert(ingredient);
        });
    }

    LiveData<Ingredient> getByUID(int uid) {
        return ingredientDao.getByUID(uid);
    }

    LiveData<List<Ingredient>> getAll() {
        return ingredientDao.getAll();
    }

    void update(Ingredient ingredient) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            ingredientDao.update(ingredient);
        });
    }

    void delete(Ingredient ingredient) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            ingredientDao.delete(ingredient);
        });
    }
}
