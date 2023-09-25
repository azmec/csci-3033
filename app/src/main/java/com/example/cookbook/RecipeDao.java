package com.example.cookbook;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe... recipes);

    @Update
    void update(Recipe... recipes);

    @Delete
    void delete(Recipe... recipes);

    @Query("DELETE FROM recipe")
    void deleteAll();

    @Query("SELECT * FROM recipe WHERE uid=:uid")
    LiveData<Recipe> getByUID(int uid);

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();
}
