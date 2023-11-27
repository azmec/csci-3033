package org.csci.mealmanual.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.csci.mealmanual.database.DomainRecipe;
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
    private LiveData<List<DomainRecipe>> allRecipes;
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

        allRecipes = LiveDataReactiveStreams.fromPublisher(
                this.recipeRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }

    /**
     * Initialize the view model's internal data repository.
     * @param context The application context necessary to initialize the repository.
     * @see Context
     */
    public void initRepository(Context context) {
        this.recipeRepository = new RecipeRepository(context);
        this.recipeTagJoinRepository = new RecipeTagJoinRepository(context);

        allRecipes = LiveDataReactiveStreams.fromPublisher(
                this.recipeRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }

    public LiveData<List<DomainRecipe>> getAllRecipes() {
        return allRecipes;
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
    public LiveData<List<DomainRecipe>> getLikedRecipes() {
        if (likedRecipes == null) {
            likedRecipes = new MutableLiveData<>();
        }
        recipeRepository.getRecipesWithTag(RecipeDatabase.LIKED_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    likedRecipes.setValue(ingredients);
                });
        return likedRecipes;
    }
}
