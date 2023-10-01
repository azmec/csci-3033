package com.example.cookbook;

import androidx.room.Entity;
import androidx.room.ForeignKey;

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
    public int ingredient_id;
    public int tag_id;

    public IngredientTagJoin(int ingredient_id, int tag_id) {
        this.ingredient_id = ingredient_id;
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return String.format("{ ingredient_id: %d, tag_id: %d }", ingredient_id, tag_id);
    }
}
