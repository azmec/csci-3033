package com.example.cookbook.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> categories;

    CategoryRepository(Context appContext) {
        RecipeDatabase db = RecipeDatabase.getInstance(appContext);
        this.categoryDao = db.getCategoryDao();
        this.categories = categoryDao.getAll();
    }

    void add(Category category) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.insert(category);
        });
    }

    LiveData<Category> getByUID(int uid) {
        return categoryDao.getByUID(uid);
    }

    LiveData<List<Category>> getAll() {
        return categoryDao.getAll();
    }

    void update(Category category) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.update(category);
        });
    }

    void delete(Category category) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.delete(category);
        });
    }
}
