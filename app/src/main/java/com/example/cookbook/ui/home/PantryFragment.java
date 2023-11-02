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
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cookbook.R;
import com.google.android.material.snackbar.Snackbar;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;


public class PantryFragment extends Fragment {
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
            // Retrieve data from EditText
            String ingredient = editTextIngredientName.getText().toString();

            // Add the ingredient to your PantryList array
            PantryList[listIdx] = ingredient;
            listIdx += 1;

            // Display the ingredient in ingredientsDisplayLayout
            TextView ingredientTextView = new TextView(getContext());
            ingredientTextView.setText(ingredient);
            ingredientTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ingredientsDisplayLayout.addView(ingredientTextView);

            // Clear the EditText
            editTextIngredientName.setText("");

            // Display some data back to the user
            //String message = "Ingredient added: " + ingredient;
            //Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();

            // Switch views to display the list of ingredients
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });
    }
}
