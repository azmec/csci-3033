package com.example.cookbook.ui.home;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

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
}

