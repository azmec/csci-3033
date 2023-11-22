package org.csci.mealmanual.database.repo;

import android.content.Context;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.RecipeIngredientJoinDao;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.model.RecipeIngredientJoin;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipeIngredientJoinRepository {
    private static final Scheduler SCHEDULER = Schedulers.io();
    private final RecipeIngredientJoinDao recipeIngredientJoinDao;

    public RecipeIngredientJoinRepository(Context context) {
        RecipeDatabase db = RecipeDatabase.getInstance(context);
        this.recipeIngredientJoinDao = db.getRecipeIngredientJoinDao();
    }

    public Completable insert(RecipeIngredientJoin... recipeIngredientJoins) {
        return recipeIngredientJoinDao.insert(recipeIngredientJoins);
    }

    public void delete(RecipeIngredientJoin... recipeIngredientJoins) {
        recipeIngredientJoinDao.delete(recipeIngredientJoins)
                .subscribeOn(SCHEDULER)
                .subscribe();
    }

    public Single<List<Ingredient>> getIngredientsInRecipe(final int recipeID) {
        return recipeIngredientJoinDao.getIngredientsInRecipe(recipeID);
    }

    public Single<List<Recipe>> getRecipesWithIngredient(final int ingredientID) {
        return recipeIngredientJoinDao.getRecipesWithIngredient(ingredientID);
    }
}
