package com.example.cookbook.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * Model for a relationship between a category and a tag <i>and</i> the
 * definition for corresponding SQLite table in the database.
 *
 * @author {Carlos Aldana Lira}
 */
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
	/**
	 * The UID of the related category.
	 */
	public int category_id;

	/**
	 * The UID of the related tag.
	 */
	public int tag_id;

	/**
	 * Construct a new relation between a category and a tag.
	 */
	public CategoryTagJoin(int category_id, int tag_id) {
		this.category_id = category_id;
		this.tag_id = tag_id;
	}

	@Override
	public String toString() {
		return String.format("{ category_id: %d, tag_id: %d }", category_id, tag_id);
	}
}
