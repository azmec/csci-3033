package com.example.cookbook.database.recipetag;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cookbook.database.tag.Tag;
import com.example.cookbook.database.recipe.Recipe;

import java.util.List;

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
	 * Insert one or more relations between a recipe and tag into the
	 * database.
	 */
	@Insert
	void insert(RecipeTagJoin... recipeTagJoins);

	/**
	 * Remove one or more relations specified by the given relations.
	 */
	@Delete
	void delete(RecipeTagJoin... recipeTagJoins);

	/**
	 * Remove all recipe-to-tag relations from the database.
	 */
	@Query("DELETE FROM recipe_tag_join")
	void deleteAll();

	/**
	 * Return the recipes related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of recipes related to the tag with the given UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM recipe " +
		"INNER JOIN recipe_tag_join " +
		" ON recipe.uid = recipe_tag_join.recipe_id " +
		"WHERE recipe_tag_join.tag_id = :tagId"
	)
	LiveData<List<Recipe>> getRecipesWithTag(final int tagId);

	/**
	 * Return the tags related to the recipe corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the recipe with the given UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM tag " +
		"INNER JOIN recipe_tag_join " +
		"ON tag.uid = recipe_tag_join.tag_id " +
		"WHERE recipe_tag_join.recipe_id = :recipeId"
	)
	LiveData<List<Tag>> getTagsWithRecipe(final int recipeId);
}
