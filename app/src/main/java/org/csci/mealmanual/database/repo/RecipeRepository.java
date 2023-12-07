package org.csci.mealmanual.database.repo;

import android.content.Context;

import org.csci.mealmanual.BuildConfig;
import org.csci.mealmanual.database.business.DomainRecipe;
import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.SpoonacularCache;
import org.csci.mealmanual.database.dao.RecipeDao;
import org.csci.mealmanual.database.dao.RecipeIngredientJoinDao;
import org.csci.mealmanual.database.dao.RecipeTagJoinDao;
import org.csci.mealmanual.database.dao.TagDao;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.util.RecipeMapper;
import org.csci.mealmanual.network.SpoonacularService;
import org.csci.mealmanual.network.SpoonacularClient;
import org.csci.mealmanual.network.model.SpoonacularRecipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository or general-purpose store for recipes.
 *
 * @author {Carlos Aldana Lira}
 */
public class RecipeRepository {

	private static final Scheduler SCHEDULER = Schedulers.io();
	private final RecipeDao recipeDao;
	private final RecipeDao cacheRecipeDao;
	private final TagDao tagDao;
	private final RecipeTagJoinDao recipeTagJoinDao;
	private final RecipeIngredientJoinDao recipeIngredientJoinDao;
	private final SpoonacularService api;

	private final String apiKey = BuildConfig.API_KEY;

	private Boolean firstQuery = true;

	/**
	 * Return a repository connected to the database.
	 *
	 * @param appContext The application's current, global context.
	 */
	public RecipeRepository(Context appContext) {
		RecipeDatabase db = RecipeDatabase.getInstance(appContext);
		SpoonacularCache cache = SpoonacularCache.getInstance(appContext);

		this.recipeDao = db.getRecipeDao();
		this.cacheRecipeDao = cache.getRecipeDao();

		this.tagDao = db.getTagDao();
		this.recipeTagJoinDao = db.getRecipeTagJoinDao();
		this.recipeIngredientJoinDao = db.getRecipeIngredientJoinDao();

		SpoonacularClient client = SpoonacularClient.getInstance();
		this.api = client.getApi();
	}

	/**
	 * Add a recipe to the repository.
	 * @param recipe The recipe to add.
	 * @return The `Single` emitting the recipe's unique identifier.
	 * @see Single
	 */
	public Single<Long> add(Recipe recipe) {
		return recipeDao.insert(recipe);
	}

	/**
	 * Add multiple recipes to the repository
	 * @param recipes The recipes to add.
	 * @return The `Single` emitting the recipes' unique identifiers.
	 * @see Single
	 */
	public Single<List<Long>> add(Recipe... recipes) {
		return recipeDao.insert(recipes);
	}

	public Single<List<Long>> addDomainRecipes(Recipe recipe, Tag... tags) {
		Single<List<Long>> insertTags = this.tagDao.insert(tags);
		Single<Long> insertRecipe = this.recipeDao.insert(recipe);

		// Accumulate the `Single`s relating the ingredient with the tags.
		Single<List<Single<Long>>> relateDomainRecipes = Single.zip(insertTags, insertRecipe, (tagIDs, recipeID) -> {
			ArrayList<Single<Long>> insertRelationsList = new ArrayList<>();
			for (long tagID : tagIDs) {
				RecipeTagJoin relation = new RecipeTagJoin(recipeID, tagID);
				Single<Long> insertRelation = this.recipeTagJoinDao.insert(relation);

				insertRelationsList.add(insertRelation);
			}

			return insertRelationsList;
		});

		// Compress the asynchronous operations into one `Single`, returning the result.
		return relateDomainRecipes.map(insertRelationsList -> {
			List<Long> rowIndices = new ArrayList<>();
			for (Single<Long> insertRelation : insertRelationsList) {
				long rowIndex = insertRelation.blockingGet();
				rowIndices.add(rowIndex);
			}

			return rowIndices;
		});
	}

	/**
	 * Return the recipe with the specified UID.
	 *
	 * @see Single
	 * @return The recipe with the specified UID in an observable container.
	 */
	public Single<Recipe> getByUID(int uid) {
		return recipeDao.getByUID(uid);
	}

	/**
	 * Return all recipes held by the repository.
	 *
	 * @return All recipes held by the repository.
	 * @see Single
	 */
	public Single<List<DomainRecipe>> getAll() {
		Single<List<Recipe>> localRecipes = this.recipeDao.getAll();
		Single<List<Recipe>> webRecipes = this.getWebRecipes();

		// When all of the above computations complete, compose their results
		// into a single list of recipes.
		Single<List<DomainRecipe>> allRecipes = Single.zip(localRecipes, webRecipes, (local, web) -> {
			List<DomainRecipe> domainRecipes = new ArrayList<>();
			for (Recipe recipe : local) {
				List<Tag> tags = this.getTagsWithRecipe(recipe).blockingGet();
				List<Ingredient> ingredients = this.getIngredientsInRecipe(recipe).blockingGet();

				DomainRecipe domainRecipe = new DomainRecipe(recipe, tags, ingredients);
				domainRecipes.add(domainRecipe);
			}

			for (Recipe recipe : web) {
				DomainRecipe domainRecipe= new DomainRecipe(recipe);
				domainRecipes.add(domainRecipe);
			}

			return domainRecipes;
		});

		return allRecipes;
	}

	/**
	 * Return a list of recipes from the web-server.
	 * @return The `Single` emitting the list of recipes from the web-server.
	 * @see Single
	 */
	private Single<List<Recipe>> getWebRecipes() {
		if (firstQuery) { // Cache is empty, so ping the server.
			firstQuery = false;

			return this.getRandomRecipe(1)
					.onErrorResumeNext(throwable -> Single.just(new ArrayList<>()))
					.map(web -> {
						// When the web content is retrieved, cache it.
						this.cacheRecipeDao.insert(web.toArray(new Recipe[0])).blockingSubscribe();
						return web;
					});
		}

		// Cache should have content from the server, so no need to ping it.
		return this.cacheRecipeDao.getAll();
	}

	/**
	 * Return a random recipe in the repository.
	 *
	 * @return A random recipe.
	 * @see Single
	 */
	public Single<List<Recipe>> getRandomRecipe(int number) {
		if (number == 0)
			return Single.just(new ArrayList<>());

		return this.api.getRandomRecipes(apiKey, number).map(response -> {
			List<SpoonacularRecipe> spoonacularRecipeList = response.getRecipeList();
			List<Recipe> recipeList = new ArrayList<>();
			for (SpoonacularRecipe spoonacularRecipe : spoonacularRecipeList) {
				Recipe recipe = RecipeMapper.mapSpoonacularRecipeToRecipe(spoonacularRecipe);
				recipeList.add(recipe);
			}

			// This is the basic structure.
			RecipeDatabase.databaseWriteExecutor.execute(() -> {
				cacheRecipeDao.insert(recipeList.toArray(new Recipe[0]));
			});

			return recipeList;
		});
	}

	/**
	 * Return the recipes with the given tag.
	 * @param tag The tag by which to select for recipes.
	 * @return The `Single` emitting the list of recipes associated with the given tag.
	 * @see Single
	 */
	public Single<List<DomainRecipe>> getRecipesWithTag(Tag tag) {
		Single<List<Recipe>> getTaggedRecipes = this.recipeTagJoinDao.getRecipesWithTag(tag.uid);
		Single<List<DomainRecipe>> mapTaggedRecipes = getTaggedRecipes.map(taggedRecipes -> {
			List<DomainRecipe> domainRecipes = new ArrayList<>();
			for (Recipe recipe : taggedRecipes) {
				List<Tag> tags = this.getTagsWithRecipe(recipe).blockingGet();
				List<Ingredient> ingredients = this.getIngredientsInRecipe(recipe).blockingGet();

				DomainRecipe domainRecipe = new DomainRecipe(recipe, tags, ingredients);
				domainRecipes.add(domainRecipe);
			}

			return domainRecipes;
		});

		return mapTaggedRecipes;
	}

	/**
	 * Return the list of tags associated with the given recipe.
	 * @param recipe The recipe whose tags to retrieve.
	 * @return The `Single` emitting the list of tags associated with the recipe.
	 * @see Single
	 */
	public Single<List<Tag>> getTagsWithRecipe(Recipe recipe) {
			return this.recipeTagJoinDao.getTagsWithRecipe(recipe.uid);
	}

	/**
	 * Return the list of ingredients associated with the given recipe.
	 * @param recipe The recipe whose ingredients to retrieve.
	 * @return The `Single` emitting the list of ingredients associated with the recipe.
	 * @see Single
	 */
	public Single<List<Ingredient>> getIngredientsInRecipe(Recipe recipe) {
		return this.recipeIngredientJoinDao.getIngredientsInRecipe(recipe.uid);
	}

	/**
	 * Update the values of a recipe in the repository. Only the recipe
	 * with the UID matching that of the given recipe will be updated.
	 *
	 * @param recipe The recipe with which to update the recipe with
	 *                 the matching UID.
	 */
	public void update(Recipe recipe) {
		recipeDao.update(recipe)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}

	/**
	 * Remove the recipe from the repository. Only the recipe with the
	 * UID matching that of the given recipe will be updated.
	 *
	 * @param recipe The recipe with which to match UID of the
	 *                 to-be-deleted recipe with.
	 */
	public void delete(Recipe recipe) {
		recipeDao.delete(recipe)
				.subscribeOn(SCHEDULER)
				.subscribe();
	}
}
