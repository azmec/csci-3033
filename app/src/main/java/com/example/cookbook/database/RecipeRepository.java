package com.example.cookbook.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cookbook.BuildConfig;
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

public class RecipeRepository {
	private RecipeDao recipeDao;
	private SpoonacularAPI api;

	private String apiKey = BuildConfig.API_KEY;
	private LiveData<List<Recipe>> recipes;

	public RecipeRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		this.recipeDao = db.getRecipeDao();
		this.recipes = recipeDao.getAll();

		SpoonacularClient client = SpoonacularClient.getInstance();
		this.api = client.getApi();
	}

	public void add(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.insert(recipe);
		});
	}

	public LiveData<Recipe> getByUID(int uid) {
		return recipeDao.getByUID(uid);
	}

	/**
	 * Return all recipes belonging to one or more of the given cuisines.
	 * @param number   The number of recipes to return.
	 * @param cuisines The list of cuisines.
	 * @return         The list recipes belonging to one more of the given cuisines.
	 */
	public LiveData<List<Recipe>> getAll(int number, String... cuisines) {
		final MutableLiveData<List<Recipe>> result = new MutableLiveData<>();
		StringBuilder cuisineList = new StringBuilder();
		for (String cuisine : cuisines)
			cuisineList.append(cuisine + ",");

		Call<ComplexSearchResponse> call = this.api.getComplexSearch(apiKey, cuisineList.toString(), number);
		call.enqueue(new Callback<ComplexSearchResponse>() {
			@Override
			public void onResponse(Call<ComplexSearchResponse> call, Response<ComplexSearchResponse> httpResponse) {
				ComplexSearchResponse response = httpResponse.body();
				List<SpoonacularRecipe> spoonacularRecipeList = response.getResults();

				List<Recipe> recipeList = new ArrayList<>();
				for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
					Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
					recipeList.add(recipe);
				}

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
	 * Return a random recipe.
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
	 * @return
	 */
	public LiveData<List<Recipe>> getAllCached() {
		return this.recipes;
	}

	/**
	 * Update a recipe's properties in the database.
	 * @param recipe The recipe to update.
	 */
	void update(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.update(recipe);
		});
	}

	/**
	 * Delete a recipe from the database.
	 * @param recipe The recipe to delete.
	 */
	void delete(Recipe recipe) {
		RecipeDatabase.databaseWriteExecutor.execute(() -> {
			recipeDao.delete(recipe);
		});
	}
}
