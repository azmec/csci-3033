package com.example.cookbook.database.category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Database access object for categories. The interface is implemented by the
 * Room library on compilation.
 *
 * @see Category
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface CategoryDao {
	/**
	 * Insert one or more categories into the database.
	 */
	@Insert
	void insert(Category... categories);

	/**
	 * Update the values of one or more categories in the database. Only
	 * categories with the same UIDs as those given will be updated.
	 */
	@Update
	void update(Category... categories);

	/**
	 * Remove one or more categories from the database. Only categories with
	 * the same UIDs as those given will be removed.
	 */
	@Delete
	void delete(Category... categories);

	/**
	 * Remove all categories from the database.
	 */
	@Query("DELETE FROM category")
	void deleteAll();

	/**
	 * Return the category with the specified UID.
	 *
	 * @see LiveData
	 * @return The category with the specified UID in an observable container.
	 */
	@Query("SELECT * FROM category WHERE uid=:uid")
	LiveData<Category> getByUID(int uid);

	/**
	 * Return all categories in the database.
	 *
	 * @see LiveData
	 * @return All categories in the database in an observable container.
	 */
	@Query("SELECT * FROM category")
	LiveData<List<Category>> getAll();
}
