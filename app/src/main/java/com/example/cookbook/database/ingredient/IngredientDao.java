package com.example.cookbook.database.ingredient;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
	Completable insert(Ingredient... ingredients);

	/**
	 * Update the values of one or more ingredients in the database. Only
	 * ingredients with the same UIDs as those given will be updated.
	 */
	@Update
	Completable update(Ingredient... ingredients);

	/**
	 * Remove one or more ingredients from the database. Only ingredients
	 * with the same UIDs as those given will be removed.
	 */
	@Delete
	Completable delete(Ingredient... ingredients);

	/**
	 * Remove all ingredients from the database.
	 */
	@Query("DELETE FROM ingredient")
	Completable deleteAll();

	/**
	 * Return the ingredient with the specified UID.
	 *
	 * @see Single
	 * @return The ingredient with the specified UID.
	 */
	@Query("SELECT * FROM ingredient WHERE uid=:uid")
	Single<Ingredient> getByUID(int uid);

	/**
	 * Return all ingredients in the database.
	 *
	 * @see Single
	 * @return All ingredients in the database.
	 */
	@Query("SELECT * FROM ingredient")
	Single<List<Ingredient>> getAll();
}
