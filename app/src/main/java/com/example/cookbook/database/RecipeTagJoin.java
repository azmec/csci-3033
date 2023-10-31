package com.example.cookbook.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.cookbook.database.recipe.Recipe;

/**
 * Model for a relationship between a recipe and a tag <i>and</i> the
 * definition for corresponding SQLite table in the database.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(
        tableName = "recipe_tag_join",
        primaryKeys = { "recipe_id", "tag_id" },
        foreignKeys = {
                @ForeignKey(
                        entity = Recipe.class,
                        parentColumns = "uid",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Tag.class,
                        parentColumns = "uid",
                        childColumns = "tag_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class RecipeTagJoin {
	/**
	 * The UID of the related recipe.
	 */
	public int recipe_id;

	/**
	 * The UID of the related tag.
	 */
	public int tag_id;

	/**
	 * Construct a new relation between a recipe and a tag.
	 */
	public RecipeTagJoin(int recipe_id, int tag_id) {
		this.recipe_id = recipe_id;
		this.tag_id = tag_id;
	}

	@Override
	public String toString() {
		return String.format("{ recipe_id: %d, tag_id: %d }", recipe_id, tag_id);
	}
}
