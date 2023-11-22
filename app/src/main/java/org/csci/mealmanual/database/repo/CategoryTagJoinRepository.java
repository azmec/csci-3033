package org.csci.mealmanual.database.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.CategoryTagJoinDao;
import org.csci.mealmanual.database.model.CategoryTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.model.Category;

import java.util.List;

/**
 * Repository or general-purpose store for category and tag relations.
 *
 * @author {Carlos Aldana Lira}
 */
public class CategoryTagJoinRepository {
	private CategoryTagJoinDao categoryTagJoinDao;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	CategoryTagJoinRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.categoryTagJoinDao = db.getCategoryTagJoinDao();
	}

	/**
	 * Add a category and tag relation to the repository.
	 *
	 * @param categoryTagJoin The relation to add.
	 */
	void add(CategoryTagJoin categoryTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			categoryTagJoinDao.insert(categoryTagJoin);
		});
	}

	/**
	 * Return the categories related to the tag corresponding to the given UID.
	 *
	 * @return The list of categories related to the tag with the given UID.
	 * @see LiveData
	 */
	LiveData<List<Category>> getCategoriesWIthTag(final int tagUID) {
		return categoryTagJoinDao.getCategoriesWithTag(tagUID);
	}

	/**
	 * Return the tags related to the category corresponding to the given UID.
	 *
	 * @return The list of tags related to the category with the given UID.
	 * @see LiveData
	 */
	LiveData<List<Tag>> getTagsWithCategory(final int categoryUID) {
		return categoryTagJoinDao.getTagsWithCategory(categoryUID);
	}

	/**
	 * Remove a category and tag relation from the repository.
	 *
	 * @param categoryTagJoin The relation to remove.
	 */
	void delete(CategoryTagJoin categoryTagJoin) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			categoryTagJoinDao.delete(categoryTagJoin);
		});
	}
}
