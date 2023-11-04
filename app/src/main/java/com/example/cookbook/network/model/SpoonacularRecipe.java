package com.example.cookbook.network.model;


import com.google.gson.annotations.SerializedName;

/**
 * Model class for recipes retrieved from the spoonacular web API.
 *
 * @author {Carlos Aldana Lira}
 */
public class SpoonacularRecipe {
	/**
	 * The unique identifier of the recipe in the spoonacular database.
	 */
	@SerializedName("id")
	private int id;

	/**
	 * The title of the recipe in the spoonacular database.
	 */
	@SerializedName("title")
	private String title;

	/**
	 * The URL to the image of the recipe in the spoonacular database.
	 */
	@SerializedName("image")
	private String imageUrl;

	/**
	 * Return the recipe's unique identifier.
	 *
	 * @return The recipe's unique identifier.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Return the recipe's title.
	 *
	 * @return The recipe's title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Return the URL to the recipe's image.
	 *
	 * @return The URL to the recipe's image.
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}

	@Override
	public String toString() {
		return String.format("Recipe: { id: %d, title: %s, imageUrl: %s", this.id, this.title, this.imageUrl);
	}
}
