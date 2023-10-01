package com.example.cookbook;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ingredient")
public class Ingredient implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "quantity")
    public final int quantity;

    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("{ uid: %d, name: %s, quantity: %d }", uid, name,  quantity);
    }
}
