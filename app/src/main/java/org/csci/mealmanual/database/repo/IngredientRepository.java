package org.csci.mealmanual.database.repo;

import android.content.Context;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.IngredientDao;
import org.csci.mealmanual.database.model.Ingredient;

import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository or general-purpose store for recipe ingredients.
 *
 * @author {Carlos Aldana Lira}
 */
public class IngredientRepository {

	private static final Scheduler SCHEDULER = Schedulers.io();
	private final IngredientDao ingredientDao;
	private Single<List<Ingredient>> ingredients;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public IngredientRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.ingredientDao = db.getIngredientDao();
		this.ingredients = ingredientDao.getAll();
	}

	/**
	 * Add a ingredient to the repository.
	 * @param ingredient The ingredient to add.
	 * @return The `Single` emitting the ingredient's unique identifier.
	 * @see Single
	 */
	public Single<Long> add(Ingredient ingredient) {
		return ingredientDao.insert(ingredient);
	}

	/**
	 * Add multiple ingredients to the repository
	 * @param ingredients The ingredients to add.
	 * @return The `Single` emitting the ingredients' unique identifiers.
	 * @see Single
	 */
	public Single<List<Long>> add(Ingredient... ingredients) {
		return ingredientDao.insert(ingredients);
	}

	/**
	 * Return the ingredient with the specified UID.
	 *
	 * @see Single
	 * @return The ingredient with the specified UID.
	 */
	public Single<Ingredient> getByUID(int uid) {
		return ingredientDao.getByUID(uid);
	}

	/**
	 * Return all ingredients in the repository.
	 *
	 * @see Single
	 * @return All ingredients in the repository in an observable
	 *         container.
	 */
	public Single<List<Ingredient>> getAll() {
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
		ingredientDao.update(ingredient)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}

	/**
	 * Remove the ingredient from the repository. Only the ingredient with
	 * the UID matching that of the given ingredient will be updated.
	 *
	 * @param ingredient The ingredient with which to match UID of the
	 *                   to-be-deleted ingredient with.
	 */
	void delete(Ingredient ingredient) {
		ingredientDao.delete(ingredient)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
