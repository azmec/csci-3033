package org.csci.mealmanual.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.csci.mealmanual.database.model.Tag;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
	 * Insert a tag into the database.
	 * @param tag The recipe to insert.
	 * @return The `Single` emitting the unique identifier of the inserted tag.
	 * @see Single
	 */
	@Insert
	Single<Long> insert(Tag tag);

	/**
	 * Insert multiple tags into the database.
	 * @param tags The tags to insert.
	 * @return The `Single` emitting the unique identifiers of the inserted tags.
	 * @see Single
	 */
	@Insert
	Single<List<Long>> insert(Tag... tags);

	/**
	 * Update the values of one or more tags in the database. Only
	 * tag with the same UIDs as those given will be updated.
	 */
	@Update
	Completable update(Tag... tags);

	/**
	 * Remove one or more tags from the database. Only tags with
	 * the same UIDs as those given will be removed.
	 */
	@Delete
	Completable delete(Tag... tags);

	/**
	 * Remove all tags from the database.
	 */
	@Query("DELETE FROM tag")
	Completable deleteAll();

	/**
	 * Return the tag with the specified UID.
	 * @see LiveData
	 * @return The tag with the specified UID.
	 */
	@Query("SELECT * FROM tag WHERE uid=:uid")
	Single<Tag> getByUID(int uid);

	/**
	 * Return all tags in the database.
	 *
	 * @see LiveData
	 * @return All tag in the database.
	 */
	@Query("SELECT * FROM tag")
	Single<List<Tag>> getAll();
}
