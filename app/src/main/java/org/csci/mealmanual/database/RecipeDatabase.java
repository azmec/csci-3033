package org.csci.mealmanual.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.csci.mealmanual.database.dao.RecipeIngredientJoinDao;
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
	version = 6,
	entities = {
		Recipe.class,
		Tag.class,
		RecipeTagJoin.class,
		Ingredient.class,
		IngredientTagJoin.class,
			RecipeIngredientJoin.class
	},
		autoMigrations = {
				@AutoMigration(from = 2, to = 3),
				@AutoMigration(from = 3, to = 4),
				@AutoMigration(from = 4, to = 5)
	}
)
public abstract class RecipeDatabase extends RoomDatabase {
	private static final String DB_FILENAME = "recipe-database.db";
	private static volatile RecipeDatabase instance;
	private static final int NUMBER_OF_THREADS = 4;

	private static final String LIKED_TAG_NAME = "Liked";
	private static final String GROCERY_TAG_NAME = "Grocery";
	private static final String PANTRY_TAG_NAME = "Pantry";

	public static final Tag LIKED_TAG = new Tag(1, LIKED_TAG_NAME);
	public static final Tag GROCERY_TAG = new Tag(2, GROCERY_TAG_NAME);
	public static final Tag PANTRY_TAG = new Tag(3, PANTRY_TAG_NAME);

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

	/*
	 * Manual migration from v. 5 to v. 6 of the database. Drops the category
	 * and category tag join tables.
	 */
	public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase db) {
			db.execSQL("DROP TABLE category");
			db.execSQL("DROP TABLE category_tag_join");
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

				long kakikID = recipeDao.insert(new Recipe("Kak'ik", "A Guatemalan soup of Mayan origin.")).blockingGet();
				long chuchitoID = recipeDao.insert(new Recipe("Chuchito", "A small, Guatemalan tamale. Often accompanied by salsa and traditional Zacapa cheese.")).blockingGet();
				long tamalitosID = recipeDao.insert(new Recipe("Tamalitos de masa", "A small, plain dough tamale.")).blockingGet();
				long caldoResID = recipeDao.insert(new Recipe("Caldo de res", "Beef and vegetable soup.")).blockingGet();
				long caldoGallinaID = recipeDao.insert(new Recipe("Caldo de gallina", "Hen soup.")).blockingGet();
				long arrozFrijolesID = recipeDao.insert(new Recipe("Arroz con frijoles", "Rice with black beans.")).blockingGet();
				long arrozFritoID = recipeDao.insert(new Recipe("Arroz frito", "Fried rice.")).blockingGet();
				long tortitasID = recipeDao.insert(new Recipe("Tortitas de yuca", "A tortilla made of yuca, which is somehow related to Jewish cuisine.")).blockingGet();

				TagDao tagDao = instance.getTagDao();
				tagDao.deleteAll();

				/*
				 * Insert "tag primitives" for liked recipes, grocery ingredients, and pantry
				 * ingredients. Because we clear the database, we guarantee the UIDs are `1`, `2`,
				 * and `3` for liked recipes, grocery ingredients, and pantry ingredients.
				 */
				tagDao.insert(new Tag(LIKED_TAG_NAME)).blockingSubscribe();
				tagDao.insert(new Tag(GROCERY_TAG_NAME)).blockingSubscribe();
				tagDao.insert(new Tag(PANTRY_TAG_NAME)).blockingSubscribe();

				long guatemalanID = tagDao.insert(new Tag("Guatemalan")).blockingGet();
				long holidayID = tagDao.insert(new Tag("Holiday")).blockingGet();
				long sideID = tagDao.insert(new Tag("Side")).blockingGet();
				long desertID = tagDao.insert(new Tag("Desert")).blockingGet();

				RecipeTagJoinDao recipeTagJoinDao = instance.getRecipeTagJoinDao();
				recipeTagJoinDao.deleteAll();

				// Recipes and tags are associated by their unique identifiers (UIDs).
				RecipeTagJoin kakikToGuatemalan = new RecipeTagJoin(kakikID, guatemalanID);
				recipeTagJoinDao.insert(kakikToGuatemalan).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(chuchitoID, guatemalanID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(tamalitosID, guatemalanID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(caldoResID, guatemalanID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(caldoGallinaID, guatemalanID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(arrozFrijolesID, sideID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(arrozFritoID, sideID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(tortitasID, sideID)).blockingSubscribe();

				recipeTagJoinDao.insert(new RecipeTagJoin(chuchitoID, holidayID)).blockingSubscribe();
				recipeTagJoinDao.insert(new RecipeTagJoin(chuchitoID, desertID)).blockingSubscribe();
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
			.addMigrations(MIGRATION_1_2, MIGRATION_5_6)
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
	 * Return a recipe-to-ingredient relation database access object (DAO)
	 * connected to this database.
	 *
	 * @return The recipe-to-ingredient relation DAO.
	 */
	public abstract RecipeIngredientJoinDao getRecipeIngredientJoinDao();
}
