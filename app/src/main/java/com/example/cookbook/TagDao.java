package com.example.cookbook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag... tags);

    @Update
    void update(Tag... tags);

    @Delete
    void delete(Tag... tags);

    @Query("SELECT * FROM tag")
    List<Tag> getAll();
}
