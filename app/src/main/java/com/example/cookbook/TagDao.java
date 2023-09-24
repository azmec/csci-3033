package com.example.cookbook;

import androidx.lifecycle.LiveData;
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

    @Query("DELETE FROM tag")
    void deleteAll();

    @Query("SELECT * FROM tag")
    LiveData<List<Tag>> getAll();
}
