package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryTagJoinDao {
    @Insert
    void insert(CategoryTagJoin... categoryTagJoins);

    @Delete
    void delete(CategoryTagJoin... categoryTagJoins);

    @Query("DELETE FROM category_tag_join")
    void deleteAll();

    @Query("SELECT * FROM category " +
            "INNER JOIN category_tag_join " +
            " ON category.uid = category_tag_join.category_id " +
            "WHERE category_tag_join.tag_id = :tagId")
    LiveData<List<Category>> getCategoriesWithTag(final int tagId);

    @Query("SELECT * FROM tag " +
            "INNER JOIN category_tag_join " +
            "ON tag.uid = category_tag_join.tag_id " +
            "WHERE category_tag_join.category_id = :categoryId")
    LiveData<List<Tag>> getTagsWithCategory(final int categoryId);
}
