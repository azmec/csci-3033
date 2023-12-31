package org.csci.mealmanual.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
	public long uid;

	/**
	 * The name of the recipe.
	 */
	@ColumnInfo(name = "name")
	public final String name;

	@ColumnInfo(name = "description")
	public String description = "";

	@ColumnInfo(name = "image_url")
	public String imageUrl;

	/**
	 * Construct a named recipe.
	 */
	public Recipe(String name) {
		this.name = name;
	}

	/**
	 * Construct a named, described recipe.
	 */
	@Ignore
	public Recipe(String name, String description) {
		this(name);

		this.description = description;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return String.format("{ id: %d, name: \"%s\", description: \"%s\" }", uid, name, description);
	}
}
