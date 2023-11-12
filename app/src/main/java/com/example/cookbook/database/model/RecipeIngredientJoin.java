package com.example.cookbook.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.model.Ingredient;

/**
 * Model for a relationship between a recipe and a tag <i>and</i> the
 * definition for the corresponding SQLite table in the database.
 */
@Entity(
        tableName = "recipe_ingredient_join",
        primaryKeys = { "recipe_id", "ingredient_id" },
        foreignKeys = {
                @ForeignKey(
                        entity = Recipe.class,
                        parentColumns = "uid",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE
                    ),
                @ForeignKey(
                        entity = Ingredient.class,
                        parentColumns = "uid",
                        childColumns = "ingredient_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class RecipeIngredientJoin {
    private final int recipe_id;
    private final int ingredient_id;

    /**
     * Construct a new relation between a recipe and an ingredient.
     * @param recipe_id     The unique identifier of the recipe.
     * @param ingredient_id The unique identifier of the ingredient.
     */
    public RecipeIngredientJoin(int recipe_id, int ingredient_id) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
    }

    /**
     * Return the unique identifier of the recipe in this relation.
     * @return The unique identifier of the recipe.
     */
    public int getRecipeID() {
        return this.recipe_id;
    }

    /**
     * Return the unique identifier of the ingredient in this relation.
     * @return The unique identifier of the ingredient.
     */
    public int getIngredientID() {
        return this.ingredient_id;
    }

    @Override
    public String toString() {
        return String.format("{ recipe_id: %d, ingredient_id: %d }", this.recipe_id, this.ingredient_id);
    }
}
