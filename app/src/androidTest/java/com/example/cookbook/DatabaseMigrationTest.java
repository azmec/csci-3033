package com.example.cookbook;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.database.RecipeDatabase;

import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Test suite for database migrations.
 *
 * @author {Carlos Aldana Lira}
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseMigrationTest {
    private static final String TEST_DB = "migration-test";

    @Rule
    public MigrationTestHelper helper;

    /**
     * Default, parameterless constructor.
     */
    public DatabaseMigrationTest() {
        helper = new MigrationTestHelper(
                InstrumentationRegistry.getInstrumentation(),
                RecipeDatabase.class
       );
    }

    /**
     * Validate database migrations from version 1 schema to version 2 schema.
     * @throws IOException Thrown when the database is not accessible.
     */
    @Test
    public void migrate1To2() throws IOException {
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 1);

        // Insert data under version 1 schema.
        ContentValues args = new ContentValues();
        args.put("name", "foo");
        db.insert("recipe", SQLiteDatabase.CONFLICT_IGNORE, args);
        db.close();

        // Migrate the database to the new version.
        RecipeDatabase migratedDb = Room.databaseBuilder(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                RecipeDatabase.class, TEST_DB).addMigrations(RecipeDatabase.MIGRATION_1_2)
                .build();
        migratedDb.getOpenHelper().getWritableDatabase();

        // Retrieve the recipe we just created.
        RecipeDao recipeDao = migratedDb.getRecipeDao();
        Single<List<Recipe>> single = recipeDao.getByUIDList(1);
        List<Recipe> recipes = single.blockingGet();
        Recipe recipe = recipes.get(0);

        // Check that the recipe's data survived migration and is what we
        // expect it to be.
        assertThat(recipes.size(), is(1));
        assertThat(recipe.name, is("foo"));
        assertThat(recipe.uid, is(1));
        assertThat(recipe.description, is(IsNull.nullValue()));
    }
}
