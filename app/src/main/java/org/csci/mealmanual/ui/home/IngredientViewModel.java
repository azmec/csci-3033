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

    /** TODO: Link grocery list to be able to send to pantry
     * NOTE: Delete the relations between ingredients and the grocery and add corresponding
     * relations between ingredients and the pantry - Carlos
     *
     * */
    private IngredientRepository ingredientRepository;
    private LiveData<List<Ingredient>> allIngredients;
    private MutableLiveData<List<Ingredient>> pantryIngredients;
    private MutableLiveData<List<Ingredient>> groceryIngredients;

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

    // Initialize the view model's internal data repository.
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

    public void updateGroceryData() {
        ingredientRepository.getIngredientsWithTag(RecipeDatabase.GROCERY_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    groceryIngredients.setValue(ingredients);
                });
    }

    public void updatePantryData() {
        ingredientRepository.getIngredientsWithTag(RecipeDatabase.PANTRY_TAG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    pantryIngredients.setValue(ingredients);
                });
    }

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

}

