package org.csci.mealmanual.database.repo;

import android.content.Context;
import android.util.Log;

import androidx.room.rxjava3.EmptyResultSetException;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.SpoonacularCache;
import org.csci.mealmanual.database.dao.RecipeDao;
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
	private RecipeDao recipeDao;

	private RecipeDao cacheRecipeDao;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public RecipeTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeTagJoinDao = db.getRecipeTagJoinDao();
		this.recipeDao = db.getRecipeDao();

		SpoonacularCache cache = SpoonacularCache.getInstance(appContext);
		this.cacheRecipeDao = cache.getRecipeDao();
	}

	/**
	 * Add a recipe and tag relation to the repository.
	 *
	 * @param recipeTagJoin The relation to add.
	 */
	public Single<Long> add(RecipeTagJoin recipeTagJoin) {
		// If the recipe is not in the database, insert it.
		Single<Long> insertRecipeIfNew = recipeDao.getByUID((int) recipeTagJoin.recipe_id)
				.flatMap(recipe -> Single.just(recipe.uid)) // Happy path: DB had recipe!
				.onErrorResumeNext(error -> { // Unhappy path: we have to insert the recipe and fetch it.
					if (error instanceof EmptyResultSetException) {
						// Fetch the recipe, delete it from cache, and insert it into disk DB.
						return cacheRecipeDao.getByUID((int) recipeTagJoin.recipe_id)
								.flatMap(cacheRecipe -> cacheRecipeDao.delete(cacheRecipe).toSingleDefault(cacheRecipe))
								.flatMap(cacheRecipe -> recipeDao.insert(cacheRecipe));

					} else {
						throw error;
					}
				});

		// Insert the relationship.
		return insertRecipeIfNew.flatMap(recipeID -> {
			RecipeTagJoin relation = new RecipeTagJoin(recipeID, recipeTagJoin.tag_id);
			return recipeTagJoinDao.insert(relation);
		});
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
