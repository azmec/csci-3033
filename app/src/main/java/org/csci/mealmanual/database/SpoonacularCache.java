package org.csci.mealmanual.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.dao.IngredientDao;
import org.csci.mealmanual.database.model.IngredientTagJoin;
import org.csci.mealmanual.database.dao.IngredientTagJoinDao;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.dao.RecipeDao;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.dao.RecipeTagJoinDao;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.dao.TagDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract definition of the database. Implemented automatically by the Room
 * library on compilation.
 */
@Database(
		version = 4,
	entities = {
		Recipe.class,
		Tag.class,
		RecipeTagJoin.class,
		Ingredient.class,
		IngredientTagJoin.class,
	},
		autoMigrations = {
				@AutoMigration(from = 1, to = 2),
				@AutoMigration(from = 2, to = 3),
		}
)
public abstract class SpoonacularCache extends RoomDatabase {
	private static volatile SpoonacularCache instance;
	private static final int NUMBER_OF_THREADS = 4;

	/*
	 * Manual migration from v. 3 to v. 4 of the database. Drops the category
	 * and category tag join tables.
	 */
	public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase db) {
			db.execSQL("DROP TABLE category");
			db.execSQL("DROP TABLE category_tag_join");
		}
	};

	/**
	 * Thread-pool responsible for writing to the database.
	 */
	public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

	/**
	 * Retrieve a reference to the single database resource.
	 *
	 * @param  appContext The Android application's context.
	 * @return            A reference to the single database resource.
	 */
	public static synchronized SpoonacularCache getInstance(Context appContext) {
		if (instance == null)
			instance = create(appContext);
		return instance;
	}

	/**
	 * Create a new database resource. Note, the returned resource will be
	 * an in-memory database.
	 *
	 * @param  appContext The Android application's context.
	 * @return            A new IN-MEMORY database resource
	 * @see               RoomDatabase
	 */
	private static SpoonacularCache create(final Context appContext) {
		return Room.inMemoryDatabaseBuilder(
			appContext,
			SpoonacularCache.class
				).addMigrations(MIGRATION_3_4)
				.build();
	}

	/**
	 * Return a recipe database access object (DAO) connected to this database.
	 *
	 * @return The recipe DAO.
	 */
	public abstract RecipeDao getRecipeDao();

	/**
	 * Return a tag database access object (DAO) connected to this database.
	 *
	 * @return The tag DAO.
	 */
	public abstract TagDao getTagDao();

	/**
	 * Return a recipe-to-tag relation database access object (DAO)
	 * connected to this database.
	 *
	 * @return The recipe-to-tag relation DAO.
	 */
	public abstract RecipeTagJoinDao getRecipeTagJoinDao();

	/**
	 * Return a ingredient database access object (DAO) connected to this
	 * database.
	 *
	 * @return The ingredient DAO.
	 */
	public abstract IngredientDao getIngredientDao();

	/**
	 * Return a ingredient-to-tag relation database access object (DAO)
	 * connected to this database.
	 *
	 * @return The ingredient-to-tag relation DAO.
	 */
	public abstract IngredientTagJoinDao getIngredientTagJoinDao();
}

