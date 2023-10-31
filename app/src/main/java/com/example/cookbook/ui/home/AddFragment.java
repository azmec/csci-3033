package com.example.cookbook.ui.home;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cookbook.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class AddFragment extends Fragment {
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
    public AddFragment() {
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
        return inflater.inflate(R.layout.add_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        EditText recipeNameEditText = view.findViewById(R.id.editTextRecipeName);
        EditText ingredientEditText = view.findViewById(R.id.editTextIngredients);
        LinearLayout ingredientsLayout = view.findViewById(R.id.ingredientsLayout);
        Button addIngredientButton = view.findViewById(R.id.buttonAddIngredient);
        Button submitButton = view.findViewById(R.id.buttonSubmit);

        //Handle the click of the "Add Ingredient" button
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limit to 15 ingredients
                if (ingredientsLayout.getChildCount() < 15) {
                    // Create a new EditText view
                    EditText newIngredientEditText = new EditText(getContext());
                    newIngredientEditText.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    newIngredientEditText.setHint("Ingredient");
                    newIngredientEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Add the new EditText to the ingredientsLayout
                    ingredientsLayout.addView(newIngredientEditText);
                } else {
                    // Inform the user about the limit
                    Snackbar.make(v, "Maximum 15 ingredients allowed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Handle the click of the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a HashMap to store the recipe data
                HashMap<String, String> recipeData = new HashMap<>();
                recipeData.put("Recipe Name", recipeNameEditText.getText().toString());
                recipeData.put("Ingredient", ingredientEditText.getText().toString());
                //... add other ingredients

                // Display some data back to the user
                String message = "Recipe Name: " + recipeData.get("Recipe Name") + "\n" +
                        "First Ingredient: " + recipeData.get("Ingredient");
                Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();

            }
        });
    }
}
