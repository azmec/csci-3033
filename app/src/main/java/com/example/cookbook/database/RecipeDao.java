package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Database access object for recipes. The interface is implemented by the
 * Room library on compilation.
 *
 * @see Recipe 
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface RecipeDao {
	/**
	 * Insert one or more recipes into the database.
	 */
	@Insert
	void insert(Recipe... recipes);

	/**
	 * Update the values of one or more recipes in the database. Only
	 * recipe with the same UIDs as those given will be updated.
	 */
	@Update
	void update(Recipe... recipes);

	/**
	 * Remove one or more recipes from the database. Only recipe with
	 * the same UIDs as those given will be removed.
	 */
	@Delete
	void delete(Recipe... recipes);

	/**
	 * Remove all recipes from the database.
	 */
	@Query("DELETE FROM recipe")
	void deleteAll();

	/**
	 * Return the recipe with the specified UID.
	 *
	 * @see LiveData
	 * @return The recipe with the specified UID in an observable container.
	 */
	@Query("SELECT * FROM recipe WHERE uid=:uid")
	LiveData<Recipe> getByUID(int uid);

	/**
	 * Return all recipes in the database.
	 *
	 * @see LiveData
	 * @return All recipe in the database in an observable container.
	 */
	@Query("SELECT * FROM recipe")
	LiveData<List<Recipe>> getAll();
}
