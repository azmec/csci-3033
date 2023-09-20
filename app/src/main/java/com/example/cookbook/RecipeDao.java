package com.example.cookbook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecipeDao {
    @Insert
    Completable insert(Recipe... recipes);

    @Update
    Completable update(Recipe... recipes);

    @Delete
    Completable delete(Recipe... recipes);

    @Query("SELECT * FROM recipe")
    Single<List<Recipe>> getAll();
}
