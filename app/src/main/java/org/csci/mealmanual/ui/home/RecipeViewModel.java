package org.csci.mealmanual.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.repo.RecipeRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository recipeRepository;
    private LiveData<List<RecipeRepository.RecipeWithTag>> allRecipes;

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
        allRecipes = LiveDataReactiveStreams.fromPublisher(
                this.recipeRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }

    public LiveData<List<RecipeRepository.RecipeWithTag>> getAllRecipes() {
        return allRecipes;
    }
}
