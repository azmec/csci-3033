package com.example.cookbook.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplexSearchResponse {
	@SerializedName("number")
	private int numResults;

	@SerializedName("results")
	private List<SpoonacularRecipe> results;

	@SerializedName("totalResults")
	private int numTotalResults;

	public List<SpoonacularRecipe> getResults() {
		return this.results;
	}

	public int getNumResults() {
		return this.numResults;
	}

	public int getNumTotalResults() {
		return this.numTotalResults;
	}
}
