// Association between recipes and tags.

package com.example.cookbook;

import androidx.room.Entity;
import androidx.room.ForeignKey;

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
    public int recipe_id;
    public int tag_id;

    public RecipeTagJoin(int recipe_id, int tag_id) {
        this.recipe_id = recipe_id;
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return String.format("{ recipe_id: %d, tag_id: %d }", recipe_id, tag_id);
    }
}
