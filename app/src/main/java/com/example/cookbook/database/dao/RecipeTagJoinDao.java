package com.example.cookbook.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cookbook.database.model.RecipeTagJoin;
import com.example.cookbook.database.model.Tag;
import com.example.cookbook.database.model.Recipe;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * Database access object for relations between recipes and tags. The
 * interface is implemented by the Room library on compilation.
 *
 * @see RecipeTagJoinDao
 * @see Recipe
 * @see Tag
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface RecipeTagJoinDao {
	/**
	 * Insert a relation between a recipe and a tag into the database.
	 * @param recipeTagJoin The recipe to insert.
	 * @return The `Single` emitting the unique identifier of the inserted relation.
	 * @see Single
	 */
	@Insert
	Single<Long> insert(RecipeTagJoin recipeTagJoin);

	/**
	 * Insert multiple relations between a recipe and a tag into the database.
	 * @param recipeTagJoins The recipeTagJoins to insert.
	 * @return The `Single` emitting the unique identifiers of the inserted relations.
	 * @see Single
	 */
	@Insert
	Single<List<Long>> insert(RecipeTagJoin... recipeTagJoins);

	/**
	 * Remove one or more relations specified by the given relations.
	 */
	@Delete
	Completable delete(RecipeTagJoin... recipeTagJoins);

	/**
	 * Remove all recipe-to-tag relations from the database.
	 */
	@Query("DELETE FROM recipe_tag_join")
	Completable deleteAll();

	/**
	 * Return the recipes related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of recipes related to the tag with the given UID.
	 * @see Single
	 */
	@Query(
		"SELECT * FROM recipe " +
		"INNER JOIN recipe_tag_join " +
		" ON recipe.uid = recipe_tag_join.recipe_id " +
		"WHERE recipe_tag_join.tag_id = :tagId"
	)
	Single<List<Recipe>> getRecipesWithTag(final int tagId);

	/**
	 * Return the tags related to the recipe corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the recipe with the given UID.
	 * @see Single
	 */
	@Query(
		"SELECT * FROM tag " +
		"INNER JOIN recipe_tag_join " +
		"ON tag.uid = recipe_tag_join.tag_id " +
		"WHERE recipe_tag_join.recipe_id = :recipeId"
	)
	Single<List<Tag>> getTagsWithRecipe(final int recipeId);
}
