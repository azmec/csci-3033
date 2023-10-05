package com.example.cookbook.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "category_tag_join",
        primaryKeys = { "category_id", "tag_id" },
        foreignKeys = {
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "uid",
                        childColumns = "category_id",
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
public class CategoryTagJoin {
    public int category_id;
    public int tag_id;

    public CategoryTagJoin(int category_id, int tag_id) {
        this.category_id = category_id;
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return String.format("{ category_id: %d, tag_id: %d }", category_id, tag_id);
    }
}
