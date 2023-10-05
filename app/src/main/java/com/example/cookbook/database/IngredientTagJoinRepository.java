package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IngredientTagJoinRepository {
    private IngredientTagJoinDao ingredientTagJoinDao;

    IngredientTagJoinRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.ingredientTagJoinDao = db.getIngredientTagJoinDao();
    }

    void add(IngredientTagJoin ingredientTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            ingredientTagJoinDao.insert(ingredientTagJoin);
        });
    }

    LiveData<List<Ingredient>> getIngredientsWIthTag(final int tagUID) {
        return ingredientTagJoinDao.getIngredientsWithTag(tagUID);
    }

    LiveData<List<Tag>> getTagsWithIngredient(final int ingredientUID) {
        return ingredientTagJoinDao.getTagsWithIngredient(ingredientUID);
    }

    void delete(IngredientTagJoin ingredientTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            ingredientTagJoinDao.delete(ingredientTagJoin);
        });
    }
}
