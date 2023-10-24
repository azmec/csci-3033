package com.example.cookbook.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Model class for recipe categories <i>and</i> definition for the
 * <code>category</code> table in the SQLite database.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(tableName = "category")
public class Category {
	/**
	 * Unique identifier for the category. It is automatically generated and
	 * assigned by the SQLite database.
	 */
	@PrimaryKey(autoGenerate = true)
	public int uid;

	/**
	 * The name of the category.
	 */
	@ColumnInfo(name = "name")
	public final String name;

	/**
	 * Construct a named category.
	 */
	public Category(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("{ uid: %d, name: %s }", uid, name);
	}
}
