package com.example.cookbook.ui.home;

import android.content.Context;
import java.util.List;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import com.example.cookbook.database.ingredient.Ingredient;
import com.example.cookbook.database.ingredient.IngredientRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IngredientViewModel extends ViewModel {

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
        ingredientRepository.add(ingredient);
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
}

