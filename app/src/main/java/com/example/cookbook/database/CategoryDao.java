package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category... categories);

    @Update
    void update(Category... categories);

    @Delete
    void delete(Category... categories);

    @Query("DELETE FROM category")
    void deleteAll();

    @Query("SELECT * FROM category WHERE uid=:uid")
    LiveData<Category> getByUID(int uid);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAll();
}
