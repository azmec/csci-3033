package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryTagJoinRepository {
    private CategoryTagJoinDao categoryTagJoinDao;

    CategoryTagJoinRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.categoryTagJoinDao = db.getCategoryTagJoinDao();
    }

    void add(CategoryTagJoin categoryTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            categoryTagJoinDao.insert(categoryTagJoin);
        });
    }

    LiveData<List<Category>> getCategoriesWIthTag(final int tagUID) {
        return categoryTagJoinDao.getCategoriesWithTag(tagUID);
    }

    LiveData<List<Tag>> getTagsWithCategory(final int categoryUID) {
        return categoryTagJoinDao.getTagsWithCategory(categoryUID);
    }

    void delete(CategoryTagJoin categoryTagJoin) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            categoryTagJoinDao.delete(categoryTagJoin);
        });
    }
}
