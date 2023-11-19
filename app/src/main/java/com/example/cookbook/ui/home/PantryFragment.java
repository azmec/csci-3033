package com.example.cookbook.ui.home;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.R;
import com.example.cookbook.database.model.Ingredient;
import com.example.cookbook.database.repo.IngredientRepository;
import com.google.android.material.snackbar.Snackbar;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PantryFragment extends Fragment {
    IngredientViewModel ingredientViewModel;
    private IngredientRepository ingredientRepository;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Ingredient> pantryList;

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

    /** Required empty public constructor */
    public PantryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        //Initialize the repository within the ViewModel
        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        ingredientViewModel.initRepository(context);

        ingredientRepository = new IngredientRepository(context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pantry_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize view model
        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        // Observe changes in the list of ingredients
        ingredientViewModel.getPantryIngredients().observe(getViewLifecycleOwner(), this::updateIngredientList);

        EditText editTextIngredientName = view.findViewById(R.id.editTextIngredientName);
        LinearLayout ingredientsDisplayLayout = view.findViewById(R.id.ingredientsDisplayLayout);
        LinearLayout addIngredientsLayout = view.findViewById(R.id.addIngredientsLayout);
        Button buttonAddToPantry = view.findViewById(R.id.buttonAddToPantry);
        Button buttonSaveIngredient = view.findViewById(R.id.buttonSaveIngredient);


        //Save ingredient to list
        buttonSaveIngredient.setOnClickListener(v -> {
            String ingredientName = editTextIngredientName.getText().toString();

            if (!ingredientName.isEmpty()) {
                Ingredient newIngredient = new Ingredient(ingredientName, 1);
                ingredientViewModel.insertIngredient(newIngredient);
                editTextIngredientName.setText(""); // Clear the EditText

                // Fetch the updated list of ingredients and update the UI
                ingredientViewModel.getPantryIngredients().getValue().add(newIngredient);
                updateIngredientList(ingredientViewModel.getPantryIngredients().getValue());
            }
            addIngredientsLayout.setVisibility(View.GONE);
        });

        /** Switching view window to 'add' */
        buttonAddToPantry.setOnClickListener(v -> {
            if (addIngredientsLayout.getVisibility() == View.VISIBLE) {
                // If already visible, hide it
                addIngredientsLayout.setVisibility(View.GONE);
            } else {
                // If not visible, make it visible
                addIngredientsLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateIngredientList(List<Ingredient> ingredients) {
        LinearLayout ingredientsDisplayLayout = getView().findViewById(R.id.ingredientsDisplayLayout);
        ingredientsDisplayLayout.removeAllViews(); // Clear the current list

        for (Ingredient ingredient : ingredients) {
            TextView textView = new TextView(getContext());
            textView.setText(ingredient.name+ " (Quantity: " + ingredient.quantity + ")"); // Display the name of the ingredient
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setPadding(16, 8, 16, 8); // Optional padding for visual appeal
            ingredientsDisplayLayout.addView(textView);
        }
    }

}
