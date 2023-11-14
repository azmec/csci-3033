package com.example.cookbook.database.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.database.dao.RecipeTagJoinDao;
import com.example.cookbook.database.model.RecipeTagJoin;
import com.example.cookbook.database.model.Tag;
import com.example.cookbook.database.model.Recipe;

import java.security.Signature;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
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
	RecipeTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeTagJoinDao = db.getRecipeTagJoinDao();
	}

	/**
	 * Add a recipe and tag relation to the repository.
	 *
	 * @param recipeTagJoin The relation to add.
	 */
	Single<Long> add(RecipeTagJoin recipeTagJoin) {
		return recipeTagJoinDao.insert(recipeTagJoin);
	}

	/**
	 * Return the recipes related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of recipes related to the tag with the given UID.
	 * @see Single
	 */
	Single<List<Recipe>> getRecipesWIthTag(final int tagUID) {
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
	Single<List<Tag>> getTagsWithRecipe(final int recipeUID) {
		return recipeTagJoinDao.getTagsWithRecipe(recipeUID);
	}

	/**
	 * Remove a recipe and tag relation from the repository.
	 *
	 * @param recipeTagJoin The relation to remove.
	 */
	void delete(RecipeTagJoin recipeTagJoin) {
		recipeTagJoinDao.delete(recipeTagJoin)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
