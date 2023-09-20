// Generic table of tags able to be associated with recipes.

package com.example.cookbook;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tag")
public class Tag implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "tag")
    public final String tag;

    public Tag(String tag) {
        this.tag = tag;
    }
}
