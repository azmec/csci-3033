package org.csci.mealmanual.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.csci.mealmanual.database.dao.RecipeIngredientJoinDao;
import org.csci.mealmanual.database.model.Category;
import org.csci.mealmanual.database.dao.CategoryDao;
import org.csci.mealmanual.database.model.CategoryTagJoin;
import org.csci.mealmanual.database.dao.CategoryTagJoinDao;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.dao.IngredientDao;
import org.csci.mealmanual.database.model.IngredientTagJoin;
import org.csci.mealmanual.database.dao.IngredientTagJoinDao;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.dao.RecipeDao;
import org.csci.mealmanual.database.model.RecipeIngredientJoin;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.dao.RecipeTagJoinDao;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.dao.TagDao;

import org.jetbrains.annotations.NotNull;

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
		Category.class,
		CategoryTagJoin.class,
			RecipeIngredientJoin.class
	},
		autoMigrations = {
				@AutoMigration(from = 2, to = 3),
				@AutoMigration(from = 3, to = 4)
	}
)
public abstract class RecipeDatabase extends RoomDatabase {
	private static final String DB_FILENAME = "recipe-database.db";
	private static volatile RecipeDatabase instance;
	private static final int NUMBER_OF_THREADS = 4;

	public static final int LIKED_UID = 5;

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

				recipeDao.insert(new Recipe("Kak'ik", "A Guatemalan soup of Mayan origin.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Chuchito", "A small, Guatemalan tamale. Often accompanied by salsa and traditional Zacapa cheese.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Tamalitos de masa", "A small, plain dough tamale.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Caldo de res", "Beef and vegetable soup.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Caldo de gallina", "Hen soup.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Arroz con frijoles", "Rice with black beans.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Arroz frito", "Fried rice.")).blockingSubscribe();
				recipeDao.insert(new Recipe("Tortitas de yuca", "A tortilla made of yuca, which is somehow related to Jewish cuisine.")).blockingSubscribe();

				TagDao tagDao = instance.getTagDao();
				tagDao.deleteAll();

				tagDao.insert(new Tag("Guatemalan")).blockingSubscribe();
				tagDao.insert(new Tag("Holiday")).blockingSubscribe();
				tagDao.insert(new Tag("Side")).blockingSubscribe();
				tagDao.insert(new Tag("Desert")).blockingSubscribe();
				tagDao.insert(new Tag("Liked")).blockingSubscribe();

				RecipeTagJoinDao recipeTagJoinDao = instance.getRecipeTagJoinDao();
				recipeTagJoinDao.deleteAll();

				// Recipes and tags are associated by their unique identifiers (UIDs).
				// Here, I know "Kak'ik" has the UID `1` because it was the first recipe
				// inserted. Similarly, I know "Guatemalan" has UID `1`. In reality,
				// the programmer will need to have a reference to the `Recipe` and
				// `Tag` objects they'd like to associate for their `uid` members.
				RecipeTagJoin kakikToGuatemalan = new RecipeTagJoin(1, 1);
				recipeTagJoinDao.insert(kakikToGuatemalan).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(2, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(3, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(4, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(5, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(6, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(7, 1)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(8, 1)).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(1, 2)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(2, 2)).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(6, 3)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(7, 3)).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(8, 4)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(8, 5)).blockingSubscribe();

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
		return Room.databaseBuilder(
			appContext,
			RecipeDatabase.class,
			DB_FILENAME
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

	/**
	 * Return a recipe-to-ingredient relation database access object (DAO)
	 * connected to this database.
	 *
	 * @return The recipe-to-ingredient relation DAO.
	 */
	public abstract RecipeIngredientJoinDao getRecipeIngredientJoinDao();
}
