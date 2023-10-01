package com.example.cookbook.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public final String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("{ uid: %d, name: %s }", uid, name);
    }
}
