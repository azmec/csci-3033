package com.example.cookbook.database.recipe;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.BuildConfig;
import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.network.SpoonacularAPI;
import com.example.cookbook.network.SpoonacularClient;
import com.example.cookbook.network.model.ComplexSearchResponse;
import com.example.cookbook.network.model.RandomRecipeResponse;
import com.example.cookbook.network.model.SpoonacularRecipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository or general-purpose store for recipes.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeRepository {
	private RecipeDao recipeDao;
	private SpoonacularAPI api;

	private String apiKey = BuildConfig.API_KEY;
	private LiveData<List<Recipe>> recipes;

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
	 * @see LiveData
	 * @return The recipe with the specified UID in an observable container.
	 */
	public LiveData<Recipe> getByUID(int uid) {
		return recipeDao.getByUID(uid);
	}

	/**
	 * Return all recipes belonging to one or more of the given cuisines.
	 *
	 * @param number   The number of recipes to return.
	 * @param cuisines The list of cuisines.
	 * @return         The list recipes belonging to one more of the given cuisines.
	 */
	public LiveData<List<Recipe>> getAll(int number, String... cuisines) {
		final MutableLiveData<List<Recipe>> result = new MutableLiveData<>();

		// The foregoing complex search requires a list of cuisines but
		// cannot itself convert a list of cuisines to a string. So, we
		// do it ourselves.
		StringBuilder cuisineList = new StringBuilder();
		for (String cuisine : cuisines)
			cuisineList.append(cuisine + ",");

		// Ping the server for recipes falling under one of the given
		// categories. If the ping fails, cancel the call. If the ping
		// succeeds, store the retrieved recipes.
		Call<ComplexSearchResponse> call = this.api.getComplexSearch(apiKey, cuisineList.toString(), number);
		call.enqueue(new Callback<ComplexSearchResponse>() {
			@Override
			public void onResponse(Call<ComplexSearchResponse> call, Response<ComplexSearchResponse> httpResponse) {
				// Convert the response into a list of recipes.
				ComplexSearchResponse response = httpResponse.body();
				List<SpoonacularRecipe> spoonacularRecipeList = response.getResults();

				// Convert the Spoonacular recipes into database recipes.
				List<Recipe> recipeList = new ArrayList<>();
				for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
					Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
					recipeList.add(recipe);
				}

				// Store the retrieved recipes in the observable container.
				result.setValue(recipeList);
			}

			@Override
			public void onFailure(Call<ComplexSearchResponse> call, Throwable t) {
				call.cancel();
			}
		});

		return result;

	}

	/**
	 * Return a random recipe in the repository.
	 *
	 * @return A random recipe.
	 */
	public LiveData<List<Recipe>> getRandomRecipe() {
		final MutableLiveData<List<Recipe>> result = new MutableLiveData<>();
		Call<RandomRecipeResponse> call = this.api.getRandomRecipes(apiKey, 1);
		call.enqueue(new Callback<RandomRecipeResponse>() {
			@Override
			public void onResponse(Call<RandomRecipeResponse> call, Response<RandomRecipeResponse> httpResponse) {
				RandomRecipeResponse response = httpResponse.body();
				List<SpoonacularRecipe> spoonacularRecipeList = response.getRecipeList();

				List<Recipe> recipeList = new ArrayList<>();
				for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
					Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
					recipeList.add(recipe);
				}

				result.setValue(recipeList);
			}

			@Override
			public void onFailure(Call<RandomRecipeResponse> call, Throwable t) {
				call.cancel();
			}
		});

		return result;
	}

	/**
	 * Return all recipes cached in the repository.
	 * 
	 * @return All recipes in this repository in an observable container.
	 */
	public LiveData<List<Recipe>> getAllCached() {
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
