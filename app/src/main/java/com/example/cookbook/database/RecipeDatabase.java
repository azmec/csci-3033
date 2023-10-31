package com.example.cookbook.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cookbook.database.category.Category;
import com.example.cookbook.database.category.CategoryDao;
import com.example.cookbook.database.categorytagjoin.CategoryTagJoin;
import com.example.cookbook.database.categorytagjoin.CategoryTagJoinDao;
import com.example.cookbook.database.ingredient.Ingredient;
import com.example.cookbook.database.ingredient.IngredientDao;
import com.example.cookbook.database.ingredienttag.IngredientTagJoin;
import com.example.cookbook.database.ingredienttag.IngredientTagJoinDao;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract definition of the database. Implemented automatically by the Room
 * library on compilation.
 */
@Database(
	version = 2,
	entities = {
		Recipe.class,
		Tag.class,
		RecipeTagJoin.class,
		Ingredient.class,
		IngredientTagJoin.class,
		Category.class,
		CategoryTagJoin.class
	}
)
public abstract class RecipeDatabase extends RoomDatabase {
	private static final String DB_FILENAME = "recipe-database.db";
	private static volatile RecipeDatabase instance;
	private static final int NUMBER_OF_THREADS = 4;

	/*
	 * Manual migration from v. 1 to v. 2 of the database.
	 */
	public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
		@Override
		public void migrate(SupportSQLiteDatabase db) {
			db.execSQL(
				"ALTER TABLE recipe " +
				"ADD COLUMN description TEXT"
			);
		}
	};

	private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
		/**
		 * Initialize the database with recipe and related attribute data.
		 */
		@Override
		public void onCreate(@NotNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			databaseWriteExecutor.execute(() -> {
				RecipeDao recipeDao = instance.getRecipeDao();
				recipeDao.deleteAll();

				recipeDao.insert(new Recipe("Kak'ik"));
				recipeDao.insert(new Recipe("Chuchito"));
				recipeDao.insert(new Recipe("Tamalitos de masa"));
				recipeDao.insert(new Recipe("Caldo de res"));
				recipeDao.insert(new Recipe("Caldo de gallina"));
				recipeDao.insert(new Recipe("Arroz con frijoles"));
				recipeDao.insert(new Recipe("Arroz frito"));
				recipeDao.insert(new Recipe("Tortitas de yuca"));

				TagDao tagDao = instance.getTagDao();
				tagDao.deleteAll();

				tagDao.insert(new Tag("Guatemalan"));
				tagDao.insert(new Tag("Holiday"));
				tagDao.insert(new Tag("Side"));
				tagDao.insert(new Tag("Desert"));

				RecipeTagJoinDao recipeTagJoinDao = instance.getRecipeTagJoinDao();
				recipeTagJoinDao.deleteAll();

				// Recipes and tags are associated by their unique identifiers (UIDs).
				// Here, I know "Kak'ik" has the UID `1` because it was the first recipe
				// inserted. Similarly, I know "Guatemalan" has UID `1`. In reality,
				// the programmer will need to have a reference to the `Recipe` and
				// `Tag` objects they'd like to associate for their `uid` members.
				RecipeTagJoin kakikToGuatemalan = new RecipeTagJoin(1, 1);
				recipeTagJoinDao.insert(kakikToGuatemalan);

				recipeTagJoinDao.insert(new RecipeTagJoin(2, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(3, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(4, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(5, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(6, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(7, 1));
				recipeTagJoinDao.insert(new RecipeTagJoin(8, 1));

				recipeTagJoinDao.insert(new RecipeTagJoin(1, 2));
				recipeTagJoinDao.insert(new RecipeTagJoin(2, 2));

				recipeTagJoinDao.insert(new RecipeTagJoin(6, 3));
				recipeTagJoinDao.insert(new RecipeTagJoin(7, 3));

				recipeTagJoinDao.insert(new RecipeTagJoin(8, 4));

				CategoryDao categoryDao = instance.getCategoryDao();
				categoryDao.deleteAll();

				categoryDao.insert(new Category("Ethnicity"));

				CategoryTagJoinDao categoryTagJoinDao = instance.getCategoryTagJoinDao();
				categoryTagJoinDao.deleteAll();

				// Relate "Ethnicity" and "Guatemalan".
				categoryTagJoinDao.insert(new CategoryTagJoin(1, 1));
			});
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
	public static synchronized RecipeDatabase getInstance(Context appContext) {
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
	private static RecipeDatabase create(final Context appContext) {
		return Room.inMemoryDatabaseBuilder(
			appContext,
			RecipeDatabase.class
			).addCallback(sRoomDatabaseCallback)
			.addMigrations(MIGRATION_1_2)
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
