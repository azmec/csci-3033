package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Database access object for tags. The interface is implemented by the
 * Room library on compilation.
 *
 * @see Tag 
 * @author {Carlos Aldana Lira}
 */
@Dao
public interface TagDao {
	/**
	 * Insert one or more tags into the database.
	 */
	@Insert
	void insert(Tag... tags);

	/**
	 * Update the values of one or more tags in the database. Only
	 * tag with the same UIDs as those given will be updated.
	 */
	@Update
	void update(Tag... tags);

	/**
	 * Remove one or more tags from the database. Only tags with
	 * the same UIDs as those given will be removed.
	 */
	@Delete
	void delete(Tag... tags);

	/**
	 * Remove all tags from the database.
	 */
	@Query("DELETE FROM tag")
	void deleteAll();

	/**
	 * Return the tag with the specified UID.
	 *
	 * @see LiveData
	 * @return The tag with the specified UID in an observable container.
	 */
	@Query("SELECT * FROM tag WHERE uid=:uid")
	LiveData<Tag> getByUID(int uid);

	/**
	 * Return all tags in the database.
	 *
	 * @see LiveData
	 * @return All tag in the database in an observable container.
	 */
	@Query("SELECT * FROM tag")
	LiveData<List<Tag>> getAll();
}
