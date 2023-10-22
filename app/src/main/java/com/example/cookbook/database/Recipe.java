package com.example.cookbook.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Model class for recipes <i>and</i> definition for the <code>recipe</code>
 * table in the SQLite database.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(tableName = "recipe")
public class Recipe implements Serializable {
	/**
	 * Unique identifier for the recipe. It is automatically generated and
	 * assigned by the SQLite database.
	 */
	@PrimaryKey(autoGenerate = true)
	public int uid;

	/**
	 * The name of the recipe.
	 */
	@ColumnInfo(name = "name")
	public final String name;

	/**
	 * Construct a named recipe.
	 */
	public Recipe(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("{ id: %d, name: \"%s\" }", uid, name);
	}
}
