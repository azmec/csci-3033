package org.csci.mealmanual.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.csci.mealmanual.database.model.IngredientTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.model.Ingredient;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
	 * Insert a relation between a ingredient and tag into the database. If the
	 * relations already exists in the database, it is replaced.
	 * @param ingredientTagJoin The relation to insert.
	 * @return The `Single` emitting the row index corresponding to the inserted
	 *         relation.
	 * @see Single
	 */
	@Insert
	Single<Long> insert(IngredientTagJoin ingredientTagJoin);

	/**
	 * Insert one or more relations between a ingredient and tag into the
	 * database. If one of the relations already exist in the database, it is
	 * replaced.
	 * @param ingredientTagJoins The relations to insert.
	 * @return The `Single` emitting the list of row indices corresponding to
	 *         the inserted relations.
	 * @see Single
	 */
	@Insert
	Single<List<Long>> insert(IngredientTagJoin... ingredientTagJoins);

	/**
	 * Remove one or more relations specified by the given relations from the
	 * database.
	 * @param ingredientTagJoins The relation(s) to remove.
	 * @return The `Completable` instance executing the removal.
	 * @see Completable
	 */
	@Delete
	Completable delete(IngredientTagJoin... ingredientTagJoins);

	/**
	 * Remove all relations between ingredients and tags from the database.
	 * @return The `Completable` instance removing all relations.
	 * @see Completable
	 */
	@Query("DELETE FROM ingredient_tag_join")
	Completable deleteAll();

	/**
	 * Return the unique identifiers of the ingredients associated with the tag
	 * represented by the given unique identifier.
	 * @param tagId The unique identifier of the tag in question.
	 * @return The unique identifiers of the associated ingredients.
	 * @see List
	 */
	@Query(
		"SELECT * FROM ingredient " +
		"INNER JOIN ingredient_tag_join " +
		" ON ingredient.uid = ingredient_tag_join.ingredient_id " +
		"WHERE ingredient_tag_join.tag_id = :tagId"
	)
	Single<List<Ingredient>> getIngredientsWithTag(final int tagId);

	/**
	 * Return the unique identifiers of the tags associated with the ingredient
	 * represented by the given unique identifier.
	 * @param ingredientId The unique identifier of the ingredient in question.
	 * @return The unique identifiers of the associated tags.
	 * @see List
	 */
	@Query(
		"SELECT * FROM tag " +
		"INNER JOIN ingredient_tag_join " +
		"ON tag.uid = ingredient_tag_join.tag_id " +
		"WHERE ingredient_tag_join.ingredient_id = :ingredientId"
	)
	Single<List<Tag>> getTagsWithIngredient(final int ingredientId);
}
