package com.example.cookbook.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cookbook.database.model.Recipe;

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
	 * Insert a recipe into the database.
	 * @param recipe The recipe to insert.
	 * @return The `Single` emitting the unique identifier of the inserted recipe.
	 * @see Single
	 */
	@Insert
	Single<Long> insert(Recipe recipe);

	/**
	 * Insert multiple recipes into the database.
	 * @param recipes The recipes to insert.
	 * @return The `Single` emitting the unique identifiers of the inserted recipes.
	 * @see Single
	 */
	@Insert
	Single<List<Long>> insert(Recipe... recipes);

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
