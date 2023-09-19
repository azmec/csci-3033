package com.example.cookbook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeTagJoinDao {
    @Insert
    void insert(RecipeTagJoin... recipeTagJoins);

    @Delete
    void delete(RecipeTagJoin... recipeTagJoins);

    @Query("SELECT * FROM recipe " +
            "INNER JOIN recipe_tag_join " +
            " ON recipe.uid = recipe_tag_join.recipe_id " +
            "WHERE recipe_tag_join.tag_id = :tagId")
    List<Recipe> getRecipesWithTag(final int tagId);

    @Query("SELECT * FROM tag " +
            "INNER JOIN recipe_tag_join " +
            "ON tag.uid = recipe_tag_join.tag_id " +
            "WHERE recipe_tag_join.recipe_id = :recipeId")
    List<Tag> getTagsWithRecipe(final int recipeId);
}
