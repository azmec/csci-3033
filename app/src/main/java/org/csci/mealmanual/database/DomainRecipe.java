package org.csci.mealmanual.database;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.model.Tag;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Immutable, domain-object recipe.
 * @author Carlos Aldana Lira
 */
public class DomainRecipe implements Serializable {
    /**
     * The recipe's unique identifier in the database.
     */
    private final long id;
    /**
     * The name of the recipe.
     */
    private final String name;
    /**
     * The description of the recipe.
     */
    private final String description;
    /**
     * The URI at which the recipe's image is located.
     */
    private final String imageURI;
    /**
     * The tags associated with the recipe.
     */
    private List<Tag> tags;
    /**
     * The ingredients in the recipe.
     */
    private List<Ingredient> ingredients;

    /**
     * Construct a new, initialized recipe.
     * @param recipe The data-model recipe from which to derive this from.
     * @param tags The tags associated with the recipe.
     * @param ingredients The ingredients in the recipe.
     */
    public DomainRecipe(Recipe recipe, List<Tag> tags, List<Ingredient> ingredients) {
        this.id = recipe.uid;
        this.name = recipe.name;
        this.description = recipe.description;
        this.imageURI = recipe.imageUrl;
        this.tags = tags;
        this.ingredients = ingredients;
    }

    /**
     * Construct a new recipe from a data-model recipe.
     * @param recipe The data-model recipe from which to derive this from.
     */
    public DomainRecipe(Recipe recipe) {
        this.id = recipe.uid;
        this.name = recipe.name;
        this.description = recipe.description;
        this.imageURI = recipe.imageUrl;
    }

    public long getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageURI() {
        return this.imageURI;
    }

    /**
     * Return an immutable list of the recipe's tags.
     * @return The tags associated with the recipe.
     */
    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    /**
     * Return an immutable list of the recipe's ingredients.
     * @return The ingredients in the recipe.
     */
    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }
}