package org.csci.mealmanual.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.repo.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IngredientViewModel extends ViewModel {
    /**OLD can remove after linking groceryList to repo
     * */
    private List<String> data;
    public List<String> getData() {
        if (data == null) {
            data = new ArrayList<String>();
        }
        return data;
    }
    public void setData(List<String> newData) {
        data = newData;
    }

    /** TODO: Link grocery list to be able to send to pantry
     *
     *
     * */
    private IngredientRepository ingredientRepository;
    private LiveData<List<Ingredient>> allIngredients;
    private MutableLiveData<List<Ingredient>> pantryIngredients;

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
        this.ingredientRepository = new IngredientRepository(context);
        allIngredients = LiveDataReactiveStreams.fromPublisher(
                ingredientRepository.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
        );
    }
    public void insertIngredient(Ingredient ingredient){
        ingredientRepository.add(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<Ingredient>> getPantryIngredients() {
        if (pantryIngredients == null) {
            pantryIngredients = new MutableLiveData<>();
            fetchPantryIngredients();
        }
        return pantryIngredients;
    }
    public void  fetchPantryIngredients() {
        ingredientRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ingredients -> {
                    pantryIngredients.setValue(ingredients);
                });
    }

    public void removeSelectedIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredientRepository.delete(ingredient);
        }
        fetchPantryIngredients(); // Fetch the updated list
    }

}
