package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TagRepository {
    private TagDao tagDao;
    private LiveData<List<Tag>> tags;

    TagRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.tagDao = db.getTagDao();
        this.tags = tagDao.getAll();
    }

    void add(Tag tag) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            tagDao.insert(tag);
        });
    }

    LiveData<Tag> getByUID(int uid) {
        return tagDao.getByUID(uid);
    }

    LiveData<List<Tag>> getAll() {
        return tagDao.getAll();
    }

    void update(Tag tag) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            tagDao.update(tag);
        });
    }

    void delete(Tag tag) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            tagDao.delete(tag);
        });
    }
}
