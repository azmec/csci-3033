package com.example.cookbook.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Model class for tags <i>and</i> definition for the <code>tag</code>
 * table in the SQLite database.
 *
 * @author {Carlos Aldana Lira}
 */
@Entity(tableName = "tag")
public class Tag implements Serializable {
	/**
	 * Unique identifier for the tag. It is automatically generated and
	 * assigned by the SQLite database.
	 */
	@PrimaryKey(autoGenerate = true)
	public long uid;

	/**
	 * The name of the tag.
	 */
	@ColumnInfo(name = "tag")
	public final String tag;

	/**
	 * Construct a named tag.
	 */
	public Tag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return String.format("{ id: %d, tag: \"%s\" }", uid, tag);
	}
}
