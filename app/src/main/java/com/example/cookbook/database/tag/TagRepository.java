package com.example.cookbook.database.tag;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.cookbook.database.RecipeDatabase;

import java.util.List;

/**
 * Repository or general-purpose store for recipe tags.
 *
 * @see Tag
 * @author {Carlos Aldana Lira}
 */
public class TagRepository {
	private TagDao tagDao;
	private LiveData<List<Tag>> tags;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	TagRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.tagDao = db.getTagDao();
		this.tags = tagDao.getAll();
	}

	/**
	 * Add a tag to the repository.
	 *
	 * @param tag The tag to add.
	 */
	void add(Tag tag) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			tagDao.insert(tag);
		});
	}

	/**
	 * Return the tag with the specified UID.
	 *
	 * @see LiveData
	 * @return The tag with the specified UID in an observable container.
	 */
	LiveData<Tag> getByUID(int uid) {
		return tagDao.getByUID(uid);
	}

	/**
	 * Return all tags in the repository.
	 *
	 * @see LiveData
	 * @return All tags in the repository in an observable container.
	 */
	LiveData<List<Tag>> getAll() {
		return tagDao.getAll();
	}

	/**
	 * Update the values of a tag in the repository. Only the
	 * tag with the UID matching that of the given tag will
	 * be updated.
	 *
	 * @param tag The tag with which to update the tag with the matching
	 *            UID.
	 */
	void update(Tag tag) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			tagDao.update(tag);
		});
	}

	/**
	 * Remove the tag from the repository. Only the tag with
	 * the UID matching that of the given tag will be updated.
	 *
	 * @param tag The tag with which to match UID of the to-be-deleted tag
	 *            with.
	 */
	void delete(Tag tag) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			tagDao.delete(tag);
		});
	}
}
