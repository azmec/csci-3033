package com.example.cookbook;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Recipe.class, Tag.class, RecipeTagJoin.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String DB_FILENAME = "recipe-database.db";

    // An individual database is expensive to create, so we want to enforce a
    // policy stipulating only one, preferably global, instance of the database
    // can exist in the program.
    private static volatile RecipeDatabase instance;
    static synchronized RecipeDatabase getInstance(Context appContext) {
        if (instance == null)
            instance = create(appContext);
        return instance;
    }

    private static RecipeDatabase create(final Context appContext) {
        return Room.databaseBuilder(
                appContext,
                RecipeDatabase.class,
                DB_FILENAME
        ).build();
    }
}
