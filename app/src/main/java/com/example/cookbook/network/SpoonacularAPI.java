package com.example.cookbook.network;

import com.example.cookbook.network.model.ComplexSearchResponse;
import com.example.cookbook.network.model.RandomRecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularAPI {
	/**
	 * Return a list of randomly selected recipes.
	 * 
	 * @param apiKey The API key with which to authorize the query.
	 * @param number The number of random recipes to return.
	 * @return       A model class for the JSON response.
	 */
	@GET("recipes/random")
	Call<RandomRecipeResponse> getRandomRecipes(
		@Query(value = "apiKey") String apiKey,
		@Query(value = "number") int number
	);

	/**
	 * Returns the list of recipes conforming to the search parameters.
	 *
	 * @param apiKey   The API key with which to authorize the query.
	 * @param cuisines A comma-separated list of cuisines to filter
	 *                 recipes by. The list of recipes returned will
	 *                 belong to one or more of the given recipes.
	 * @param number   The number of recipes to return.
	 * @return         A model class for the JSON response.
	 */
	@GET("recipes/complexSearch")
	Call<ComplexSearchResponse> getComplexSearch(
		@Query(value = "apiKey") String apiKey,
		@Query(value = "cuisine", encoded = true) String cuisines,
		@Query(value = "number") int number
	);
}
