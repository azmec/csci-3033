package com.example.cookbook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cookbook.R;
import com.google.android.material.snackbar.Snackbar;


public class PantryFragment extends Fragment {

    public PantryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pantry_fragment, container, false);
    }

    private int listIdx = 0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextIngredientName = view.findViewById(R.id.editTextIngredientName);
        LinearLayout currentIngredientsLayout = view.findViewById(R.id.currentIngredientsLayout);
        LinearLayout addIngredientsLayout = view.findViewById(R.id.addIngredientsLayout);
        Button buttonAddToPantry = view.findViewById(R.id.buttonAddToPantry);
        Button buttonViewPantry = view.findViewById(R.id.buttonViewPantry);
        Button buttonSaveIngredient = view.findViewById(R.id.buttonSaveIngredient);

        //Switching view window to 'add'
        buttonAddToPantry.setOnClickListener(v -> {
            currentIngredientsLayout.setVisibility(View.GONE);
            addIngredientsLayout.setVisibility(View.VISIBLE);
        });
        //Switching view window to 'List'
        buttonViewPantry.setOnClickListener(v -> {
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });

        //array to store ingredients
        String[] PantryList = new String[200];

        //Save ingredient to list
        buttonSaveIngredient.setOnClickListener(v -> {
            // Retrieve data from EditText
            PantryList[listIdx] = editTextIngredientName.getText().toString();

            // Display some data back to the user
            String message = "Ingredient added: " + PantryList[listIdx];
            Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();

            //increment list index
            listIdx += 1;
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });

    }
}
