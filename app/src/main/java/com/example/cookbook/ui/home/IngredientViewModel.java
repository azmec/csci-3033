package com.example.cookbook.ui.home;

import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import com.example.cookbook.database.model.Ingredient;
import com.example.cookbook.database.repo.IngredientRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

    public LiveData<List<Ingredient>> getAllIngredients() {
        return allIngredients;
    }
}

