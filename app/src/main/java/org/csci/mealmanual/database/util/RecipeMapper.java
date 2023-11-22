package org.csci.mealmanual.database.util;

import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.network.model.SpoonacularRecipe;

/**
 * Mapper of <code>network.model.SpoonacularRecipe</code> objects to
 * <code>database.Recipe</code> objects.
 *
 * @see SpoonacularRecipe
 * @see Recipe
 * @author {Carlos Aldana Lira}
 */
public class RecipeMapper {
	/**
	 * Map a `SpoonacularRecipe` to a local, serializable `Recipe`.
	 * 
	 * @param spoonacularRecipe A recipe from the spooacular web API.
	 * @return                  A local, serializable recipe.
	 */
	static public Recipe mapSpoonacularRecipeToRecipe(SpoonacularRecipe spoonacularRecipe) {
		Recipe recipe = new Recipe(spoonacularRecipe.getTitle(), "");

		return recipe;
    }
}
