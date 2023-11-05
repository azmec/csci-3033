package com.example.cookbook.database.recipe;

import android.content.Context;

import com.example.cookbook.BuildConfig;
import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.network.SpoonacularService;
import com.example.cookbook.network.SpoonacularClient;
import com.example.cookbook.network.model.SpoonacularRecipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Repository or general-purpose store for recipes.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeRepository {
	private final RecipeDao recipeDao;
	private final SpoonacularService api;

	private final String apiKey = BuildConfig.API_KEY;
	private Single<List<Recipe>> recipes;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public RecipeRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeDao = db.getRecipeDao();
		this.recipes = recipeDao.getAll();

		SpoonacularClient client = SpoonacularClient.getInstance();
		this.api = client.getApi();
	}

	/**
	 * Add a recipe to the repository.
	 *
	 * @param recipe The recipe to add.
	 */
	public void add(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.insert(recipe);
		});
	}

	/**
	 * Return the recipe with the specified UID.
	 *
	 * @see Single
	 * @return The recipe with the specified UID in an observable container.
	 */
	public Single<Recipe> getByUID(int uid) {
		return recipeDao.getByUID(uid);
	}

	/**
	 * Return all recipes belonging to one or more of the given cuisines.
	 *
	 * @param number   The number of recipes to return.
	 * @param cuisines The list of cuisines.
	 * @return         The list recipes belonging to one more of the given cuisines.
	 * @see Single
	 */
	public Single<List<Recipe>> getAll(int number, String... cuisines) {
		// The foregoing complex search requires a list of cuisines but
		// cannot itself convert a list of cuisines to a string. So, we
		// do it ourselves.
		StringBuilder cuisineList = new StringBuilder();
		for (String cuisine : cuisines)
			cuisineList.append(cuisine + ",");

		return this.api.getComplexSearch(apiKey, cuisineList.toString(), number).map(response -> {
			List<SpoonacularRecipe> spoonacularRecipeList = response.getResults();
			List<Recipe> recipeList = new ArrayList<>();
			for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
				Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
				recipeList.add(recipe);
			}

			return recipeList;
		});
	}

	/**
	 * Return a random recipe in the repository.
	 *
	 * @return A random recipe.
	 * @see Single
	 */
	public Single<List<Recipe>> getRandomRecipe() {
		return this.api.getRandomRecipes(apiKey, 1).map(response -> {
			List<SpoonacularRecipe> spoonacularRecipeList = response.getRecipeList();
			List<Recipe> recipeList = new ArrayList<>();
			for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
				Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
				recipeList.add(recipe);
			}

			return recipeList;
		});
	}

	/**
	 * Return all recipes cached in the repository.
	 * 
	 * @return All recipes in this repository.
	 * @see Single
	 */
	public Single<List<Recipe>> getAllCached() {
		return this.recipes;
	}

	/**
	 * Update the values of a recipe in the repository. Only the recipe
	 * with the UID matching that of the given recipe will be updated.
	 *
	 * @param recipe The recipe with which to update the recipe with
	 *                 the matching UID.
	 */
	public void update(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.update(recipe);
		});
	}

	/**
	 * Remove the recipe from the repository. Only the recipe with the
	 * UID matching that of the given recipe will be updated.
	 *
	 * @param recipe The recipe with which to match UID of the
	 *                 to-be-deleted recipe with.
	 */
	public void delete(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.delete(recipe);
		});
	}
}
