package org.csci.mealmanual.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.repo.IngredientRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IngredientViewModel extends ViewModel {
    private IngredientRepository ingredientRepository;
    private LiveData<List<Ingredient>> allIngredients;
    private MutableLiveData<List<Ingredient>> pantryIngredients;
    private MutableLiveData<List<Ingredient>> groceryIngredients;

    /** Required empty public constructor */
    public IngredientViewModel() {}

    // Construct and return a view model with an initialized data repository.
    public IngredientViewModel(Context context) {
        ingredientRepository = new IngredientRepository(context);
        allIngredients = LiveDataReactiveStreams.fromPublisher(
                ingredientRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }

    // Initialize the view model's internal data repository and the two corresponding lists
    public void initRepository(Context context) {
        this.groceryIngredients = new MutableLiveData<>();
        this.pantryIngredients = new MutableLiveData<>();

        this.ingredientRepository = new IngredientRepository(context);
        allIngredients = LiveDataReactiveStreams.fromPublisher(
                ingredientRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }
    public LiveData<List<Ingredient>> getAllIngredients(){return allIngredients;}

    //Takes an ingredient name and a tag for which list the ingredient belongs to
    // and adds the ingredient into the repository
    public void insertTaggedIngredient(Ingredient ingredient, Tag... tags){
        ingredientRepository.addTaggedIngredient(ingredient, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(indices -> {
                    this.updateGroceryData();
                    this.updatePantryData();
                });
    }

    public LiveData<List<Ingredient>> getGroceryData() {
        return this.groceryIngredients;
    }

    public LiveData<List<Ingredient>> getPantryData() {
        return this.pantryIngredients;
    }

    //Updates the list of ingredients that have the grocery tag
    public void updateGroceryData() {
        ingredientRepository.getIngredientsWithTag(RecipeDatabase.GROCERY_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    groceryIngredients.setValue(ingredients);
                });
    }

    //Updates the list of ingredients that have the pantry tag
    public void updatePantryData() {
        ingredientRepository.getIngredientsWithTag(RecipeDatabase.PANTRY_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    pantryIngredients.setValue(ingredients);
                });
    }

    //Takes a list of ingredients and removes them from the repository
    public void removeSelectedIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredientRepository.delete(ingredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        this.updatePantryData();
                        this.updateGroceryData();
                    });
        }
    }

    // Change tags from grocery to pantry for each ingredient.
    public void transferToPantry(List<Ingredient> ingredients){
        // TODO: Ideally, we compress all these asyncs to a single `Completable` and subscribe only
        // to that, but having a callback for each individual meme works too.
        for(Ingredient ingredient: ingredients){
            ingredientRepository.removeTagFromIngredient(ingredient, RecipeDatabase.GROCERY_TAG)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        this.updatePantryData();
                        this.updateGroceryData();
                    });
            ingredientRepository.addTagToIngredient(ingredient, RecipeDatabase.PANTRY_TAG)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(indices -> {
                        this.updatePantryData();
                        this.updateGroceryData();
                    });
        }
    }
}

