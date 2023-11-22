package org.csci.mealmanual.database.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.dao.CategoryDao;
import org.csci.mealmanual.database.model.Category;

import java.util.List;

/**
 * Repository or general-purpose store for recipe categories.
 *
 * @author {Carlos Aldana Lira}
 */
public class CategoryRepository {
	private CategoryDao categoryDao;
	private LiveData<List<Category>> categories;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	CategoryRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.categoryDao = db.getCategoryDao();
		this.categories = categoryDao.getAll();
	}

	/**
	 * Add a category to the repository.
	 *
	 * @param category The category to add.
	 */
	void add(Category category) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			categoryDao.insert(category);
		});
	}

	/**
	 * Return the category with the specified UID.
	 *
	 * @see LiveData
	 * @return The category with the specified UID in an observable container.
	 */
	LiveData<Category> getByUID(int uid) {
		return categoryDao.getByUID(uid);
	}

	/**
	 * Return all categories in the repository.
	 *
	 * @see LiveData
	 * @return All categories in the repository in an observable container.
	 */
	LiveData<List<Category>> getAll() {
		return categoryDao.getAll();
	}

	/**
	 * Update the values of a category in the repository. Only the category
	 * with the UID matching that of the given category will be updated.
	 *
	 * @param category The category with which to update the category with
	 *                 the matching UID.
	 */
	void update(Category category) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			categoryDao.update(category);
		});
	}

	/**
	 * Remove the category from the repository. Only the category with the
	 * UID matching that of the given category will be updated.
	 *
	 * @param category The category with which to match UID of the
	 *                 to-be-deleted category with.
	 */
	void delete(Category category) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			categoryDao.delete(category);
		});
	}
}
