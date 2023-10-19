package com.example.cookbook.database;

import com.example.cookbook.network.model.SpoonacularRecipe;

public class RecipeMapper {
	/**
	 * Map a `SpoonacularRecipe` to a local, serializable `Recipe`.
	 * 
	 * @param spoonacularRecipe A recipe from the spooacular web API.
	 * @return                  A local, serializable recipe.
	 */
	static public Recipe mapSpoonacularRecipeToRecipe(SpoonacularRecipe spoonacularRecipe) {
		Recipe recipe = new Recipe(spoonacularRecipe.getTitle());

		return recipe;
    }
}
