package com.example.cookbook.network.model;

import com.google.gson.annotations.SerializedName;

public class SpoonacularRecipe {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public String toString() {
        return String.format("Recipe: { id: %d, title: %s, imageUrl: %s", this.id, this.title, this.imageUrl);
    }
}
