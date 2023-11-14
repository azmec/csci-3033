package com.example.cookbook.database.repo;

import android.content.Context;

import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.database.dao.RecipeIngredientJoinDao;
import com.example.cookbook.database.model.Ingredient;
import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.model.RecipeIngredientJoin;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class RecipeIngredientJoinRepository {
    private final RecipeIngredientJoinDao recipeIngredientJoinDao;

    public RecipeIngredientJoinRepository(Context context) {
        RecipeDatabase db = RecipeDatabase.getInstance(context);
        this.recipeIngredientJoinDao = db.getRecipeIngredientJoinDao();
    }

    public Completable insert(RecipeIngredientJoin... recipeIngredientJoins) {
        return recipeIngredientJoinDao.insert(recipeIngredientJoins);
    }

    public Completable delete(RecipeIngredientJoin... recipeIngredientJoins) {
        return recipeIngredientJoinDao.delete(recipeIngredientJoins);
    }

    public Single<List<Ingredient>> getIngredientsInRecipe(final int recipeID) {
        return recipeIngredientJoinDao.getIngredientsInRecipe(recipeID);
    }

    public Single<List<Recipe>> getRecipesWithIngredient(final int ingredientID) {
        return recipeIngredientJoinDao.getRecipesWithIngredient(ingredientID);
    }
}
