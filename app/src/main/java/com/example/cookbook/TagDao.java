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
public interface TagDao {
    @Insert
    Completable insert(Tag... tags);

    @Update
    Completable update(Tag... tags);

    @Delete
    Completable delete(Tag... tags);

    @Query("SELECT * FROM tag")
    Single<List<Tag>> getAll();
}
