package org.csci.mealmanual.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.model.RecipeIngredientJoin;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * Database access object for relations between recipes and ingredients. The
 * interface is implemented by the Room library at compile-time.
 *
 * @see RecipeIngredientJoin
 */
@Dao
public interface RecipeIngredientJoinDao {
    /**
     * Insert one or more relations between a recipe and an ingredient into
     * the database.
     * @param recipeIngredientJoins The relation(s) to insert.
     * @return The `Completable` instance executing the insertion.
     * @see Completable
     */
    @Insert
    public Completable insert(RecipeIngredientJoin... recipeIngredientJoins);

    /**
     * Remove one or more relations specified by the given relations from the
     * database.
     * @param recipeIngredientJoins The relation(s) to remove.
     * @return The `Completable` instance executing the removal.
     * @see Completable
     */
    @Delete
    public Completable delete(RecipeIngredientJoin... recipeIngredientJoins);

    /**
     * Remove all relations from the database.
     * @return The `Completable` instance removing all relations.
     * @see Completable
     */
    @Query("DELETE FROM recipe_ingredient_join")
    public Completable deleteAll();

    @Query(
            "SELECT * FROM ingredient " +
                    "INNER JOIN recipe_ingredient_join " +
                    "ON ingredient.uid = recipe_ingredient_join.ingredient_id " +
                    "WHERE recipe_ingredient_join.recipe_id = :recipeId"
    )
    /**
     * Return the unique identifiers of the ingredients in the recipe represented
     * by the given unique identifier.
     * @param recipeId The unique identifier of the recipe in question.
     * @return The unique identifiers of ingredients in the given recipe.
     * @see Ingredient
     */
    public Single<List<Ingredient>> getIngredientsInRecipe(final int recipeId);

    /**
     * Return the unique identifiers of the recipes with the ingredient represented
     * by the given unique identifier.
     * @param ingredientId The unique identifier of the ingredient in question.
     * @return The unique identifiers of recipes with the given ingredient.
     * @see Recipe
     */
    @Query(
            "SELECT * FROM recipe " +
                    "INNER JOIN recipe_ingredient_join " +
                    "ON recipe.uid = recipe_ingredient_join.recipe_id " +
                    "WHERE recipe_ingredient_join.ingredient_id = :ingredientId"
    )
    public Single<List<Recipe>> getRecipesWithIngredient(final int ingredientId);
}
