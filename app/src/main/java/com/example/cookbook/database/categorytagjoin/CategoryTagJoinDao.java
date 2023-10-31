package com.example.cookbook.database.categorytagjoin;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cookbook.database.Tag;
import com.example.cookbook.database.category.Category;

import java.util.List;

/**
 * Database access object for relations between categories and tags. The
 * interface is implemented by the Room library on compilation.
 *
 * @see CategoryTagJoinDao
 * @see Category
 * @see Tag
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface CategoryTagJoinDao {
	/**
	 * Insert one or more relations between a category and tag into the
	 * database.
	 */
	@Insert
	void insert(CategoryTagJoin... categoryTagJoins);

	/**
	 * Remove one or more relations specified by the given relations.
	 */
	@Delete
	void delete(CategoryTagJoin... categoryTagJoins);

	/**
	 * Remove all relations from the database.
	 */
	@Query("DELETE FROM category_tag_join")
	void deleteAll();

	/**
	 * Return the categories related to the tag corresponding to the given UID.
	 *
	 * @return The list of categories related to the tag with the given UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM category " +
		"INNER JOIN category_tag_join " +
		" ON category.uid = category_tag_join.category_id " +
		"WHERE category_tag_join.tag_id = :tagId"
	)
	LiveData<List<Category>> getCategoriesWithTag(final int tagId);

	/**
	 * Return the tags related to the category corresponding to the given UID.
	 *
	 * @return The list of tags related to the category with the given UID.
	 * @see LiveData
	 */
	@Query(
		"SELECT * FROM tag " +
		"INNER JOIN category_tag_join " +
		"ON tag.uid = category_tag_join.tag_id " +
		"WHERE category_tag_join.category_id = :categoryId"
	)
	LiveData<List<Tag>> getTagsWithCategory(final int categoryId);
}
