package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Repository or general-purpose store for recipe and tag relations.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeTagJoinRepository {
	private RecipeTagJoinDao recipeTagJoinDao;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	RecipeTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeTagJoinDao = db.getRecipeTagJoinDao();
	}

	/**
	 * Add a recipe and tag relation to the repository.
	 *
	 * @param recipeTagJoin The relation to add.
	 */
	void add(RecipeTagJoin recipeTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeTagJoinDao.insert(recipeTagJoin);
		});
	}

	/**
	 * Return the recipes related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of recipes related to the tag with the given UID.
	 * @see LiveData
	 */
	LiveData<List<Recipe>> getRecipesWIthTag(final int tagUID) {
		return recipeTagJoinDao.getRecipesWithTag(tagUID);
	}

	/**
	 * Return the tags related to the recipe corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the recipe with the given
	 *         UID.
	 * @see LiveData
	 */
	LiveData<List<Tag>> getTagsWithRecipe(final int recipeUID) {
		return recipeTagJoinDao.getTagsWithRecipe(recipeUID);
	}

	/**
	 * Remove a recipe and tag relation from the repository.
	 *
	 * @param recipeTagJoin The relation to remove.
	 */
	void delete(RecipeTagJoin recipeTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeTagJoinDao.delete(recipeTagJoin);
		});
	}
}
