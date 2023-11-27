package org.csci.mealmanual.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.csci.mealmanual.R;
import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class GroceryFragment extends Fragment {
    IngredientViewModel ingredientViewModel;
    private ArrayList<Ingredient> selectedIngredients = new ArrayList<>();

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
    public GroceryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        //Initialize the repository within the ViewModel
        ingredientViewModel = new ViewModelProvider(requireActivity()).get(IngredientViewModel.class);
        ingredientViewModel.initRepository(context);

        // Observe changes in the list of ingredients
        final Observer<List<Ingredient>> observer = ingredients -> {
            updateIngredientList(ingredients);
        };
        ingredientViewModel.getGroceryData().observe(this, observer);
        ingredientViewModel.updateGroceryData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grocery_fragment, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //Initialize UI elements
        EditText groceryIngredient = view.findViewById(R.id.editTextGrocery);
        LinearLayout addGroceriesLayout = view.findViewById(R.id.addGroceriesLayout);

        Button buttonTransfer = view.findViewById(R.id.buttonTransferToPantry);
        Button buttonAddToGrocery = view.findViewById(R.id.buttonAddGroceries);
        Button buttonSaveIngredient = view.findViewById(R.id.buttonSaveGroceries);
        Button buttonRemoveIngredient = view.findViewById(R.id.buttonRemove);

        buttonSaveIngredient.setOnClickListener(v->{
            String ingredientName = groceryIngredient.getText().toString();

            if (!ingredientName.isEmpty()) {
                Ingredient newIngredient = new Ingredient(ingredientName, "", 1);
                ingredientViewModel.insertTaggedIngredient(newIngredient, RecipeDatabase.GROCERY_TAG);
                groceryIngredient.setText(""); // Clear the EditText
            }
            else {
                Snackbar.make(v, "Nothing added", Snackbar.LENGTH_SHORT).show();}
            addGroceriesLayout.setVisibility(View.GONE);
        });

        buttonTransfer.setOnClickListener(v->{
            ingredientViewModel.transferToPantry(selectedIngredients);
            selectedIngredients.clear();

            updateIngredientList(ingredientViewModel.getGroceryIngredients().getValue());
        });

        /** Toggle view window to 'add' */
        buttonAddToGrocery.setOnClickListener(v -> {
            if (addGroceriesLayout.getVisibility() == View.VISIBLE) {
                // If already visible, hide it
                addGroceriesLayout.setVisibility(View.GONE);
            } else {
                // If not visible, make it visible
                addGroceriesLayout.setVisibility(View.VISIBLE);
            }
        });

        buttonRemoveIngredient.setOnClickListener(v->{
            ingredientViewModel.removeSelectedIngredients(selectedIngredients);
            selectedIngredients.clear();

            updateIngredientList(ingredientViewModel.getGroceryIngredients().getValue());
        });
    }

    private void updateIngredientList(List<Ingredient> ingredients) {
        LinearLayout ingredientsDisplayLayout = getView().findViewById(R.id.ingredientsDisplayLayout);
        ingredientsDisplayLayout.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Ingredient ingredient: ingredients){
            View itemView = inflater.inflate(R.layout.select_ingredient_list, ingredientsDisplayLayout, false);
            CheckBox checkBox = itemView.findViewById(R.id.checkBoxIngredient);
            TextView textView = itemView.findViewById(R.id.textViewIngredient);

            textView.setText(ingredient.name); // Display the name of the ingredient

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedIngredients.add(ingredient);
                } else {
                    selectedIngredients.remove(ingredient);
                }
            });

            ingredientsDisplayLayout.addView(itemView);
        }
    }
}
