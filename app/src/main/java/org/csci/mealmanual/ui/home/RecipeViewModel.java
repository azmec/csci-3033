package org.csci.mealmanual.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.csci.mealmanual.database.business.DomainRecipe;
import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.repo.RecipeRepository;
import org.csci.mealmanual.database.repo.RecipeTagJoinRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository recipeRepository;
    private RecipeTagJoinRepository recipeTagJoinRepository;

    private MutableLiveData<List<DomainRecipe>> recipes;
    private MutableLiveData<List<DomainRecipe>> likedRecipes;

    /**
     * Default, parameterless constructor.
     */
    public RecipeViewModel() {}

    /**
     * Construct and return a view model with an initialized data repository.
     * @param context The application context necessary to initialize the repository.
     * @see Context
     */
    public RecipeViewModel(Context context) {
        recipeRepository = new RecipeRepository(context);
        recipeTagJoinRepository = new RecipeTagJoinRepository(context);

        this.recipes = new MutableLiveData<>();
        this.likedRecipes = new MutableLiveData<>();

        this.updateRecipeData();
    }

    /**
     * Initialize the view model's internal data repository.
     * @param context The application context necessary to initialize the repository.
     * @see Context
     */
    public void initRepository(Context context) {
        this.recipeRepository = new RecipeRepository(context);
        this.recipeTagJoinRepository = new RecipeTagJoinRepository(context);

        this.recipes = new MutableLiveData<>();
        this.likedRecipes = new MutableLiveData<>();

        this.updateRecipeData();
        this.updateLikedRecipeData();
    }

    public LiveData<List<DomainRecipe>> getRecipeData() {
        return this.recipes;
    }

    public LiveData<List<DomainRecipe>> getLikedRecipeData() {
        return this.likedRecipes;
    }

    public void updateRecipeData() {
        this.recipeRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeList -> {
                    this.recipes.setValue(recipeList);
                });
    }

    public void likeRecipe(DomainRecipe recipe) {
        RecipeTagJoin relation = new RecipeTagJoin(recipe.getID(), RecipeDatabase.LIKED_TAG.uid);
        this.recipeTagJoinRepository.add(relation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void removeLike(DomainRecipe recipe){
        RecipeTagJoin relation = new RecipeTagJoin(recipe.getID(), RecipeDatabase.LIKED_TAG.uid);
        this.recipeTagJoinRepository.delete(relation);
    }

    /** Getting all recipes with the liked tag
     * */
    public void updateLikedRecipeData() {
        this.recipeRepository.getRecipesWithTag(RecipeDatabase.LIKED_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeList -> {
                    this.likedRecipes.setValue(recipeList);
                });
    }
}
