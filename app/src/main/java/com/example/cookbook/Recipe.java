// Entity model for recipes. Each instance of `Recipe` represents a row in the
// table `recipe` in the SQLite database.

package com.example.cookbook;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {
    // `autoGenerate` forces the database to assign the next available
    // uniquely-identifying integer to the recipe.
    //
    // NOTE: How do this work with the below initialization function?
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public final String name;

    public Recipe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, name: \"%s\" }", uid, name);
    }
}
