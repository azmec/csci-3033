package com.example.cookbook.database.ingredient;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Database access object for ingredients. The interface is implemented by the
 * Room library on compilation.
 *
 * @see Ingredient 
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface IngredientDao {
	/**
	 * Insert one or more ingredients into the database.
	 */
	@Insert
	void insert(Ingredient... ingredients);

	/**
	 * Update the values of one or more ingredients in the database. Only
	 * ingredients with the same UIDs as those given will be updated.
	 */
	@Update
	void update(Ingredient... ingredients);

	/**
	 * Remove one or more ingredients from the database. Only ingredients
	 * with the same UIDs as those given will be removed.
	 */
	@Delete
	void delete(Ingredient... ingredients);

	/**
	 * Remove all ingredients from the database.
	 */
	@Query("DELETE FROM ingredient")
	void deleteAll();

	/**
	 * Return the ingredient with the specified UID.
	 *
	 * @see LiveData
	 * @return The ingredient with the specified UID in an observable
	 *         container.
	 */
	@Query("SELECT * FROM ingredient WHERE uid=:uid")
	LiveData<Ingredient> getByUID(int uid);

	/**
	 * Return all ingredients in the database.
	 *
	 * @see LiveData
	 * @return All ingredients in the database in an observable container.
	 */
	@Query("SELECT * FROM ingredient")
	LiveData<List<Ingredient>> getAll();
}
