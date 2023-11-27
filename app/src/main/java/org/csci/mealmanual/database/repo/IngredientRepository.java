package org.csci.mealmanual.database.repo;

import android.content.Context;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.IngredientDao;
import org.csci.mealmanual.database.dao.IngredientTagJoinDao;
import org.csci.mealmanual.database.dao.TagDao;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.IngredientTagJoin;
import org.csci.mealmanual.database.model.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
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
	private final TagDao tagDao;
	private final IngredientTagJoinDao ingredientTagJoinDao;
	private Single<List<Ingredient>> ingredients;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public IngredientRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.ingredientDao = db.getIngredientDao();
		this.tagDao = db.getTagDao();
		this.ingredientTagJoinDao = db.getIngredientTagJoinDao();

		this.ingredients = ingredientDao.getAll();
	}

	/**
	 * Insert the ingredient into the database, relating it with the given tags.
	 * @param ingredient The ingredient to insert.
	 * @param tags The tags to associate with the ingredient.
	 * @return The `Single` emitting the row indices corresponding to the relations corresponing
	 *         to the given ingredient and tags.
	 * @see Single
	 */
	public Single<List<Long>> addTaggedIngredient(Ingredient ingredient, Tag... tags) {
		// TODO: Make this not an obvious ad-hoc hack. Replacements invoke cascades.
		List<Long> tagIds = new ArrayList<>();
		for (Tag tag : tags)
			tagIds.add(tag.uid);

		// Ensure the database contains the tags we'd like to associate.
		Single<List<Long>> insertTags = Single.just(tagIds);
		Single<Long> insertIngredient = this.ingredientDao.insert(ingredient);

		// Accumulate the `Single`s relating the ingredient with the tags.
		Single<List<Single<Long>>> relateIngredientTags = Single.zip(insertTags, insertIngredient, (tagIds2, ingredientID) -> {
			ArrayList<Single<Long>> insertRelationsList = new ArrayList<>();

			// Initialize the `Single`s corresponding to relating each tag with the ingredient.
			for (long tagID : tagIds2) {
				IngredientTagJoin relation = new IngredientTagJoin(ingredientID, tagID);
				Single<Long> insertRelation = this.ingredientTagJoinDao.insert(relation);

				insertRelationsList.add(insertRelation);
			}

			return insertRelationsList;
		});

		// Compress the list of accumulated `Single`s into one `Single`
		// inserting the tags, inserting the ingredient, and relating the tags
		// with the ingredient.
		return relateIngredientTags.flatMap(insertRelationsList -> Single.zipArray(objects -> {
			// Explicitly cast each individual `Object` into a `long`.
			List<Long> rowIndices = new ArrayList<>();
			for (Object object : objects) {
				long rowIndex = (long)object;
				rowIndices.add(rowIndex);
			}

			return rowIndices;
		}, insertRelationsList.toArray(new Single[0])));
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
	 * Return the ingredients with the given tag. If the tag is not in the
	 * database, it is added to the database.
	 * @param tag The tag to select for ingredients by.
	 * @return The `Single` emitting the list of ingredients associated with the tag.
	 * @see Single
	 */
	public Single<List<Ingredient>> getIngredientsWithTag(Tag tag) {
		return this.ingredientTagJoinDao.getIngredientsWithTag(tag.uid);
	}

	public Completable removeTagFromIngredient(Ingredient ingredient, Tag tag) {
		IngredientTagJoin relation = new IngredientTagJoin(ingredient.uid, tag.uid);
		return this.ingredientTagJoinDao.delete(relation);
	}

	public Single<Long> addTagToIngredient(Ingredient ingredient, Tag tag) {
		IngredientTagJoin relation = new IngredientTagJoin(ingredient.uid, tag.uid);
		return this.ingredientTagJoinDao.insert(relation);
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
	public Completable delete(Ingredient ingredient) {
		return ingredientDao.delete(ingredient);
	}
}
