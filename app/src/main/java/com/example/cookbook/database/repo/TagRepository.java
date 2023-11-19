package com.example.cookbook.database.repo;

import android.content.Context;

import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.database.dao.TagDao;
import com.example.cookbook.database.model.Tag;

import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository or general-purpose store for recipe tags.
 *
 * @see Tag
 * @author {Carlos Aldana Lira}
 */
public class TagRepository {

	private static final Scheduler SCHEDULER = Schedulers.io();
	private TagDao tagDao;
	private Single<List<Tag>> tags;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public TagRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.tagDao = db.getTagDao();
		this.tags = tagDao.getAll();
	}

	/**
	 * Add a tag to the repository.
	 *
	 * @param tag The tag to add.
	 */
	public Single<Long> add(Tag tag) {
		return tagDao.insert(tag);
	}

	/**
	 * Return the tag with the specified UID.
	 *
	 * @see Single
	 * @return The tag with the specified UID.
	 */
	public Single<Tag> getByUID(int uid) {
		return tagDao.getByUID(uid);
	}

	/**
	 * Return all tags in the repository.
	 *
	 * @see Single
	 * @return All tags in the repository.
	 */
	public Single<List<Tag>> getAll() {
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
	public void update(Tag tag) {
		tagDao.update(tag)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}

	/**
	 * Remove the tag from the repository. Only the tag with
	 * the UID matching that of the given tag will be updated.
	 *
	 * @param tag The tag with which to match UID of the to-be-deleted tag
	 *            with.
	 */
	public void delete(Tag tag) {
		tagDao.delete(tag)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
