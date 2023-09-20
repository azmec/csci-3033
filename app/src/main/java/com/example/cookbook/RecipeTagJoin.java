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
                        onDelete = 0x5 // CASCADE
                ),
                @ForeignKey(
                        entity = Tag.class,
                        parentColumns = "uid",
                        childColumns = "tag_id",
                        onDelete = 0x5 // CASCADE
                )
        }
)
public class RecipeTagJoin {
    public int recipe_id;
    public int tag_id;
}
