package com.example.cookbook.ui.home;

import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GroceryViewModel extends ViewModel {
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

    // Method to transfer item to pantry
/*    public void transferToPantry(Ingredient ingredient) {
        ingredient.setInPantry(true);
        ingredientRepository.update(ingredient);
    }*/
}

