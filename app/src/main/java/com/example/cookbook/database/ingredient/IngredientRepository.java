package com.example.cookbook.database.ingredient;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.cookbook.database.RecipeDatabase;

import java.util.List;

/**
 * Repository or general-purpose store for recipe ingredients.
 *
 * @author {Carlos Aldana Lira}
 */
public class IngredientRepository {
	private IngredientDao ingredientDao;
	private LiveData<List<Ingredient>> ingredients;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	IngredientRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.ingredientDao = db.getIngredientDao();
		this.ingredients = ingredientDao.getAll();
	}

	/**
	 * Add a ingredient to the repository.
	 *
	 * @param ingredient The ingredient to add.
	 */
	void add(Ingredient ingredient) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			ingredientDao.insert(ingredient);
		});
	}

	/**
	 * Return the ingredient with the specified UID.
	 *
	 * @see LiveData
	 * @return The ingredient with the specified UID in an observable
	 *         container.
	 */
	LiveData<Ingredient> getByUID(int uid) {
		return ingredientDao.getByUID(uid);
	}

	/**
	 * Return all ingredients in the repository.
	 *
	 * @see LiveData
	 * @return All ingredients in the repository in an observable
	 *         container.
	 */
	LiveData<List<Ingredient>> getAll() {
		return ingredientDao.getAll();
	}

	/**
	 * Update the values of a ingredient in the repository. Only the
	 * ingredient with the UID matching that of the given ingredient will
	 * be updated.
	 *
	 * @param ingredient The ingredient with which to update the ingredient
	 *                   with the matching UID.
	 */
	void update(Ingredient ingredient) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			ingredientDao.update(ingredient);
		});
	}

	/**
	 * Remove the ingredient from the repository. Only the ingredient with
	 * the UID matching that of the given ingredient will be updated.
	 *
	 * @param ingredient The ingredient with which to match UID of the
	 *                   to-be-deleted ingredient with.
	 */
	void delete(Ingredient ingredient) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			ingredientDao.delete(ingredient);
		});
	}
}