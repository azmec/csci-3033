package org.csci.mealmanual.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Model class for recipe ingredients <i>and</i> definition for the
 * <code>ingredient</code> table in the SQLite database.
 *
 * TODO: Implement a "unit" column.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(tableName = "ingredient")
public class Ingredient implements Serializable {
	/**
	 * Unique identifier for the ingredient. It is automatically generated
	 * and assigned by the SQLite database.
	 */
	@PrimaryKey(autoGenerate = true)
	public long uid;

	/**
	 * The name of the ingredient.
	 */
	@ColumnInfo(name = "name")
	public final String name;

	@ColumnInfo(name = "unit")
	public final String unit;

	/**
	 * The quantity of the ingredient.
	 */
	@ColumnInfo(name = "quantity")
	public final int quantity;

	/**
	 * Construct a named, quantized ingredient.
	 */
	public Ingredient(String name, String unit, int quantity) {
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return String.format("{ uid: %d, name: %s, quantity: %d }", uid, name,  quantity);
	}
}
