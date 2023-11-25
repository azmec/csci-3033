package org.csci.mealmanual.database.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.IngredientTagJoinDao;
import org.csci.mealmanual.database.model.IngredientTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.model.Ingredient;

import java.util.List;

/**
 * Repository or general-purpose store for ingredient and tag relations.
 *
 * @author {Carlos Aldana Lira}
 */
public class IngredientTagJoinRepository {
	private IngredientTagJoinDao ingredientTagJoinDao;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	IngredientTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.ingredientTagJoinDao = db.getIngredientTagJoinDao();
	}

	/**
	 * Add a ingredient and tag relation to the repository.
	 *
	 * @param ingredientTagJoin The relation to add.
	 */
	void add(IngredientTagJoin ingredientTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			ingredientTagJoinDao.insert(ingredientTagJoin);
		});
	}

	/**
	 * Return the ingredients related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of ingredients related to the tag with the given UID.
	 * @see LiveData
	 */
	LiveData<List<Ingredient>> getIngredientsWIthTag(final int tagUID) {
		return ingredientTagJoinDao.getIngredientsWithTag(tagUID);
	}

	/**
	 * Return the tags related to the ingredient corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the ingredient with the given
	 *         UID.
	 * @see LiveData
	 */
	LiveData<List<Tag>> getTagsWithIngredient(final int ingredientUID) {
		return ingredientTagJoinDao.getTagsWithIngredient(ingredientUID);
	}

	/**
	 * Remove a ingredient and tag relation from the repository.
	 *
	 * @param ingredientTagJoin The relation to remove.
	 */
	void delete(IngredientTagJoin ingredientTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			ingredientTagJoinDao.delete(ingredientTagJoin);
		});
	}
}