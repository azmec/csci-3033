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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cookbook.R;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PantryFragment extends Fragment {
    IngredientViewModel ingredientViewModel;
    private List<String> pantryList = new ArrayList<>();
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    public PantryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the repository within the ViewModel
        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        ingredientViewModel.initRepository(getContext());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pantry_fragment, container, false);
    }

    //array to store ingredients
    private int listIdx = 0;
    String[] PantryList = new String[200];

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextIngredientName = view.findViewById(R.id.editTextIngredientName);
        LinearLayout currentIngredientsLayout = view.findViewById(R.id.ingredientsDisplayLayout);
        LinearLayout addIngredientsLayout = view.findViewById(R.id.addIngredientsLayout);
        Button buttonAddToPantry = view.findViewById(R.id.buttonAddToPantry);
        Button buttonViewPantry = view.findViewById(R.id.buttonViewPantry);
        Button buttonSaveIngredient = view.findViewById(R.id.buttonSaveIngredient);


        //Switching view window to 'add'
        buttonAddToPantry.setOnClickListener(v -> {
            currentIngredientsLayout.setVisibility(View.GONE);
            addIngredientsLayout.setVisibility(View.VISIBLE);
        });

        LinearLayout ingredientsDisplayLayout = view.findViewById(R.id.ingredientsDisplayLayout);

        buttonViewPantry.setOnClickListener(v -> {
            ingredientsDisplayLayout.removeAllViews(); // Clear previous views


            //Retrieve the data stored in the view model and update the list accordingly
            for (int i = 0; i < ingredientViewModel.getData().size(); i++) {
                PantryList[i] = ingredientViewModel.getData().get(i);
                listIdx = i + 1;
            }

            for (int i = 0; i < listIdx; i++) {
                TextView textView = new TextView(getContext());
                textView.setText(PantryList[i]);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setPadding(16, 8, 16, 8); // optional padding for better visual appeal
                ingredientsDisplayLayout.addView(textView);
            }
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });

        //Save ingredient to list
        buttonSaveIngredient.setOnClickListener(v -> {
            String ingredient = editTextIngredientName.getText().toString();

            // Add the ingredient to your PantryList array
            /*
            listIdx += 1;
            PantryList[listIdx] = ingredient;
            */

            // Get the current list of ingredients from the ViewModel
            List<String> currentIngredients = ingredientViewModel.getData();

            // Add the ingredient to the ViewModel's list
            currentIngredients.add(ingredient);

            // Update the list in the ViewModel
            ingredientViewModel.setData(currentIngredients);

            // Display the ingredient in ingredientsDisplayLayout
            TextView ingredientTextView = new TextView(getContext());
            ingredientTextView.setText(ingredient);
            ingredientTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ingredientsDisplayLayout.addView(ingredientTextView);

            // Switch views to display the list of ingredients
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });
    }
}
