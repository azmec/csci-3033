package com.example.cookbook.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Recipe.class,
        Tag.class,
        RecipeTagJoin.class,
        Ingredient.class,
        IngredientTagJoin.class,
        Category.class,
        CategoryTagJoin.class
}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String DB_FILENAME = "recipe-database.db";
    private static volatile RecipeDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
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

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Retrieve a reference to the single database resource.
     * @param  appContext The Android application's context.
     * @return            A reference to the single database resource.
     */
    static synchronized RecipeDatabase getInstance(Context appContext) {
        if (instance == null)
            instance = create(appContext);
        return instance;
    }

    /**
     * Create a new database resource. Note, the returned resource will be an
     * IN-MEMORY database.
     * @param  appContext The Android application's context.
     * @return            A new IN-MEMORY database resource
     * @see               RoomDatabase
     */
    private static RecipeDatabase create(final Context appContext) {
        return Room.inMemoryDatabaseBuilder(
                appContext,
                RecipeDatabase.class
        ).addCallback(sRoomDatabaseCallback).build();
    }

    public abstract RecipeDao getRecipeDao();
    public abstract TagDao getTagDao();
    public abstract RecipeTagJoinDao getRecipeTagJoinDao();

    public abstract IngredientDao getIngredientDao();

    public abstract IngredientTagJoinDao getIngredientTagJoinDao();

    public abstract CategoryDao getCategoryDao();

    public abstract CategoryTagJoinDao getCategoryTagJoinDao();
}