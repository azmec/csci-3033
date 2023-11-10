package com.example.cookbook.database.recipe;

import android.content.Context;

import com.example.cookbook.BuildConfig;
import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.database.SpoonacularCache;
import com.example.cookbook.network.SpoonacularService;
import com.example.cookbook.network.SpoonacularClient;
import com.example.cookbook.network.model.SpoonacularRecipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository or general-purpose store for recipes.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeRepository {

	private static final Scheduler SCHEDULER = Schedulers.io();
	private final RecipeDao recipeDao;
	private final RecipeDao cacheRecipeDao;
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
		SpoonacularCache cache = SpoonacularCache.getInstance(appContext);

		this.recipeDao = db.getRecipeDao();
		this.cacheRecipeDao = cache.getRecipeDao();
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
		recipeDao.insert(recipe)
				.subscribeOn(SCHEDULER)
				.subscribe();
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
	 * Return all recipes held by the repository.
	 *
	 * @return All recipes held by the repository.
	 * @see Single
	 */
	public Single<List<Recipe>> getAll() {
		Single<List<Recipe>> localRecipes = this.recipeDao.getAll();
		Single<List<Recipe>> cachedRecipes = this.cacheRecipeDao.getAll();
		//Single<List<Recipe>> webRecipes = getRandomRecipe(5);
		Single<List<Recipe>> webRecipes = Single.just(new ArrayList<>());

		// When all of the above computations complete, compose their results
		// into a single list of recipes.
		Single<List<Recipe>> allRecipes = Single.zip(localRecipes, cachedRecipes, webRecipes, (local, cached, web) -> {
			List<Recipe> recipes = new ArrayList<>();
			recipes.addAll(local);
			recipes.addAll(cached);
			recipes.addAll(web);

			return recipes;
		});

		return allRecipes;
	}

	/**
	 * Return a random recipe in the repository.
	 *
	 * @return A random recipe.
	 * @see Single
	 */
	public Single<List<Recipe>> getRandomRecipe(int number) {
		return this.api.getRandomRecipes(apiKey, number).map(response -> {
			List<SpoonacularRecipe> spoonacularRecipeList = response.getRecipeList();
			List<Recipe> recipeList = new ArrayList<>();
			for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
				Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
				recipeList.add(recipe);
			}

			// This is the basic structure.
			RecipeDatabase.databaseWriteExecutor.execute(() -> {
				cacheRecipeDao.insert(recipeList.toArray(new Recipe[0]));
			});

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
		recipeDao.update(recipe)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}

	/**
	 * Remove the recipe from the repository. Only the recipe with the
	 * UID matching that of the given recipe will be updated.
	 *
	 * @param recipe The recipe with which to match UID of the
	 *                 to-be-deleted recipe with.
	 */
	public void delete(Recipe recipe) {
		recipeDao.delete(recipe)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
