package org.csci.mealmanual.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for the response retrieved from the spoonacular web API when
 * querying for a random recipe.
 *
 * @author {Carlos Aldana Lira}
 */
public class RandomRecipeResponse {
	/**
	 * The list of returned random reicpes.
	 *
	 * @see SpoonacularRecipe
	 */
	@SerializedName("recipes")
	private List<SpoonacularRecipe> recipeList;

	/**
	 * Return the list of returned random recipes.
	 *
	 * @return The list of returned random recipes.
	 * @see SpoonacularRecipe
	 */
	public List<SpoonacularRecipe> getRecipeList() {
		return this.recipeList;
	}
}
