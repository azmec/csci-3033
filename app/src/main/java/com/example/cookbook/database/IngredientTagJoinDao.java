package com.example.cookbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientTagJoinDao {
    @Insert
    void insert(IngredientTagJoin... ingredientTagJoins);

    @Delete
    void delete(IngredientTagJoin... ingredientTagJoins);

    @Query("DELETE FROM ingredient_tag_join")
    void deleteAll();

    @Query("SELECT * FROM ingredient " +
            "INNER JOIN ingredient_tag_join " +
            " ON ingredient.uid = ingredient_tag_join.ingredient_id " +
            "WHERE ingredient_tag_join.tag_id = :tagId")
    LiveData<List<Ingredient>> getIngredientsWithTag(final int tagId);

    @Query("SELECT * FROM tag " +
            "INNER JOIN ingredient_tag_join " +
            "ON tag.uid = ingredient_tag_join.tag_id " +
            "WHERE ingredient_tag_join.ingredient_id = :ingredientId")
    LiveData<List<Tag>> getTagsWithIngredient(final int ingredientId);
}
