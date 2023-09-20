package com.example.cookbook;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecipeTagJoinDao {
    @Insert
    Completable insert(RecipeTagJoin... recipeTagJoins);

    @Delete
    Completable delete(RecipeTagJoin... recipeTagJoins);

    @Query("SELECT * FROM recipe " +
            "INNER JOIN recipe_tag_join " +
            " ON recipe.uid = recipe_tag_join.recipe_id " +
            "WHERE recipe_tag_join.tag_id = :tagId")
    Single<List<Recipe>> getRecipesWithTag(final int tagId);

    @Query("SELECT * FROM tag " +
            "INNER JOIN recipe_tag_join " +
            "ON tag.uid = recipe_tag_join.tag_id " +
            "WHERE recipe_tag_join.recipe_id = :recipeId")
    Single<List<Tag>> getTagsWithRecipe(final int recipeId);
}
