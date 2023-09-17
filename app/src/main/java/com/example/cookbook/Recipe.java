// Entity model for recipes. Each instance of `Recipe` represents a row in the
// table `recipe` in the SQLite database.

package com.example.cookbook;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;
}
