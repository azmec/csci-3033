package com.example.cookbook.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cookbook.database.model.Category;
import com.example.cookbook.database.dao.CategoryDao;
import com.example.cookbook.database.model.CategoryTagJoin;
import com.example.cookbook.database.dao.CategoryTagJoinDao;
import com.example.cookbook.database.model.Ingredient;
import com.example.cookbook.database.dao.IngredientDao;
import com.example.cookbook.database.model.IngredientTagJoin;
import com.example.cookbook.database.dao.IngredientTagJoinDao;
import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.dao.RecipeDao;
import com.example.cookbook.database.model.RecipeTagJoin;
import com.example.cookbook.database.dao.RecipeTagJoinDao;
import com.example.cookbook.database.model.Tag;
import com.example.cookbook.database.dao.TagDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract definition of the database. Implemented automatically by the Room
 * library on compilation.
 */
@Database(
	entities = {
		Recipe.class,
		Tag.class,
		RecipeTagJoin.class,
		Ingredient.class,
		IngredientTagJoin.class,
		Category.class,
		CategoryTagJoin.class
	},
	version = 1
)
public abstract class SpoonacularCache extends RoomDatabase {
	private static volatile SpoonacularCache instance;
	private static final int NUMBER_OF_THREADS = 4;

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
			).build();
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

	/**
	 * Return a category database access object (DAO) connected to this
	 * database.
	 *
	 * @return The category DAO.
	 */
	public abstract CategoryDao getCategoryDao();

	/**
	 * Return a category-to-tag relation database access object (DAO)
	 * connected to this database.
	 *
	 * @return The category-to-tag relation DAO.
	 */
	public abstract CategoryTagJoinDao getCategoryTagJoinDao();
}

