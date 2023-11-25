package org.csci.mealmanual.database.repo;

import android.content.Context;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.RecipeTagJoinDao;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.model.Recipe;

import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository or general-purpose store for recipe and tag relations.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeTagJoinRepository {

	private static final Scheduler SCHEDULER = Schedulers.io();
	private RecipeTagJoinDao recipeTagJoinDao;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public RecipeTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeTagJoinDao = db.getRecipeTagJoinDao();
	}

	/**
	 * Add a recipe and tag relation to the repository.
	 *
	 * @param recipeTagJoin The relation to add.
	 */
	public Single<Long> add(RecipeTagJoin recipeTagJoin) {
		return recipeTagJoinDao.insert(recipeTagJoin);
	}

	public Single<List<Long>> add(RecipeTagJoin... recipeTagJoins) {
		return recipeTagJoinDao.insert(recipeTagJoins);
	}

	/**
	 * Return the recipes related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of recipes related to the tag with the given UID.
	 * @see Single
	 */
	public Single<List<Recipe>> getRecipesWIthTag(final int tagUID) {
		return recipeTagJoinDao.getRecipesWithTag(tagUID);
	}

	/**
	 * Return the tags related to the recipe corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the recipe with the given
	 *         UID.
	 * @see Single
	 */
	public Single<List<Tag>> getTagsWithRecipe(final int recipeUID) {
		return recipeTagJoinDao.getTagsWithRecipe(recipeUID);
	}

	/**
	 * Remove a recipe and tag relation from the repository.
	 *
	 * @param recipeTagJoin The relation to remove.
	 */
	public void delete(RecipeTagJoin recipeTagJoin) {
		recipeTagJoinDao.delete(recipeTagJoin)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
