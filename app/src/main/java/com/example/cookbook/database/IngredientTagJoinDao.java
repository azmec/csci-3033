package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Database access object for relations between ingredients and tags. The
 * interface is implemented by the Room library on compilation.
 *
 * @see IngredientTagJoinDao
 * @see Ingredient 
 * @see Tag
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface IngredientTagJoinDao {
	/**
	 * Insert one or more relations between a ingredient and tag into the
	 * database.
	 */
	@Insert
	void insert(IngredientTagJoin... ingredientTagJoins);

	/**
	 * Remove one or more relations specified by the given relations.
	 */
	@Delete
	void delete(IngredientTagJoin... ingredientTagJoins);

	/**
	 * Remove all relations from the database.
	 */
	@Query("DELETE FROM ingredient_tag_join")
	void deleteAll();

	/**
	 * Return the ingredients related to the tag corresponding to the given
	 * UID.
	 *
	 * @return The list of ingredients related to the tag with the given
	 *         UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM ingredient " +
		"INNER JOIN ingredient_tag_join " +
		" ON ingredient.uid = ingredient_tag_join.ingredient_id " +
		"WHERE ingredient_tag_join.tag_id = :tagId"
	)
	LiveData<List<Ingredient>> getIngredientsWithTag(final int tagId);

	/**
	 * Return the tags related to the ingredient corresponding to the given
	 * UID.
	 *
	 * @return The list of tags related to the ingredient with the given
	 *         UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM tag " +
		"INNER JOIN ingredient_tag_join " +
		"ON tag.uid = ingredient_tag_join.tag_id " +
		"WHERE ingredient_tag_join.ingredient_id = :ingredientId"
	)
	LiveData<List<Tag>> getTagsWithIngredient(final int ingredientId);
}
