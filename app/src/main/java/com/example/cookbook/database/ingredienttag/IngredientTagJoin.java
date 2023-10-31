package com.example.cookbook.database.ingredienttag;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.cookbook.database.tag.Tag;
import com.example.cookbook.database.ingredient.Ingredient;

/**
 * Model for a relationship between a ingredient and a tag <i>and</i> the
 * definition for corresponding SQLite table in the database.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(
        tableName = "ingredient_tag_join",
        primaryKeys = { "ingredient_id", "tag_id" },
        foreignKeys = {
                @ForeignKey(
                        entity = Ingredient.class,
                        parentColumns = "uid",
                        childColumns = "ingredient_id",
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
public class IngredientTagJoin {
	/**
	 * The UID of the related ingredient.
	 */
	public int ingredient_id;

	/**
	 * The UID of the related tag.
	 */
	public int tag_id;

	/**
	 * Construct a new relation between a ingredient and a tag.
	 */
	public IngredientTagJoin(int ingredient_id, int tag_id) {
		this.ingredient_id = ingredient_id;
		this.tag_id = tag_id;
	}

	@Override
	public String toString() {
		return String.format("{ ingredient_id: %d, tag_id: %d }", ingredient_id, tag_id);
	}
}
