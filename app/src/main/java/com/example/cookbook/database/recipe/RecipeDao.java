package com.example.cookbook.database.recipe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
	Completable insert(Recipe... recipes);

	/**
	 * Update the values of one or more recipes in the database. Only
	 * recipe with the same UIDs as those given will be updated.
	 */
	@Update
	Completable update(Recipe... recipes);

	/**
	 * Remove one or more recipes from the database. Only recipe with
	 * the same UIDs as those given will be removed.
	 */
	@Delete
	Completable delete(Recipe... recipes);

	/**
	 * Remove all recipes from the database.
	 */
	@Query("DELETE FROM recipe")
	Completable deleteAll();

	/**
	 * Return the recipe with the specified UID.
	 *
	 * @see Single
	 * @return The recipe with the specified UID in an observable container.
	 */
	@Query("SELECT * FROM recipe WHERE uid=:uid")
	Single<Recipe> getByUID(int uid);

	/**
	 * Return the recipe with the specified UID.
	 *
	 * @return The recipe with the specified UID.
	 */
	@Query("SELECT * FROM recipe WHERE uid=:uid")
	Single<List<Recipe>> getByUIDList(int uid);

	/**
	 * Return all recipes in the database.
	 *
	 * @see Single
	 * @return All recipe in the database in an observable container.
	 */
	@Query("SELECT * FROM recipe")
	Single<List<Recipe>> getAll();
}
