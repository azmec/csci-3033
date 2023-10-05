
package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeTagJoinRepository {
    private RecipeTagJoinDao recipeTagJoinDao;

    RecipeTagJoinRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.recipeTagJoinDao = db.getRecipeTagJoinDao();
    }

    void add(RecipeTagJoin recipeTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            recipeTagJoinDao.insert(recipeTagJoin);
        });
    }

    LiveData<List<Recipe>> getRecipesWIthTag(final int tagUID) {
        return recipeTagJoinDao.getRecipesWithTag(tagUID);
    }

    LiveData<List<Tag>> getTagsWithRecipe(final int recipeUID) {
        return recipeTagJoinDao.getTagsWithRecipe(recipeUID);
    }

    void delete(RecipeTagJoin recipeTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            recipeTagJoinDao.delete(recipeTagJoin);
        });
    }
}
