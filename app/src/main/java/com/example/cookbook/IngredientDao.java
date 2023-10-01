package com.example.cookbook;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient... ingredients);

    @Update
    void update(Ingredient... ingredients);

    @Delete
    void delete(Ingredient... ingredients);

    @Query("DELETE FROM ingredient")
    void deleteAll();

    @Query("SELECT * FROM ingredient WHERE uid=:uid")
    LiveData<Ingredient> getByUID(int uid);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAll();
}
