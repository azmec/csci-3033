package com.example.cookbook.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for the response retrieved from the spoonacular web API when
 * querying for a recipe through a complex search.
 *
 * @author {Carlos Aldana Lira}
 */
public class ComplexSearchResponse {
	/**
	 * The number of recipes contained in the response.
	 */
	@SerializedName("number")
	private int numResults;

	/**
	 * The list of returned recipes.
	 *
	 * @see SpoonacularRecipe
	 */
	@SerializedName("results")
	private List<SpoonacularRecipe> results;

	/**
	 * The total number of recipes that qualified for the query. This is
	 * not necessarily equivalent to `this.numResults`.
	 */
	@SerializedName("totalResults")
	private int numTotalResults;

	/**
	 * Return the list of returned recipes.
	 *
	 * @return The list of returned recipes.
	 * @see SpoonacularRecipe
	 */
	public List<SpoonacularRecipe> getResults() {
		return this.results;
	}

	/**
	 * Return the number of returned results.
	 *
	 * @return The number of returned results.
	 */
	public int getNumResults() {
		return this.numResults;
	}

	/**
	 * Return the number of recipes that qualified for the query.
	 *
	 * @return The number of recipes that qualified for the query.
	 */
	public int getNumTotalResults() {
		return this.numTotalResults;
	}
}
