package com.example.cookbook.ui.home;

import android.content.Context;
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
import com.example.cookbook.database.RecipeDatabase;
import com.example.cookbook.database.dao.RecipeIngredientJoinDao;
import com.example.cookbook.database.model.Ingredient;
import com.example.cookbook.database.model.RecipeIngredientJoin;
import com.example.cookbook.database.repo.IngredientRepository;
import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.repo.RecipeIngredientJoinRepository;
import com.example.cookbook.database.repo.RecipeRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddFragment extends Fragment {
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private RecipeIngredientJoinRepository recipeIngredientJoinRepository;

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
        Context context = getContext();
        this.recipeRepository = new RecipeRepository(context);
        this.ingredientRepository = new IngredientRepository(context);
        this.recipeIngredientJoinRepository = new RecipeIngredientJoinRepository(context);
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
        EditText recipeDescriptionEditText = view.findViewById(R.id.editTextRecipeDescription);
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

                // Create recipe and send to the repository
                String name = recipeNameEditText.getText().toString();
                String description = recipeDescriptionEditText.getText().toString();
                String ingredientName = ingredientEditText.getText().toString();
                Recipe recipe = new Recipe(name, description);
                Ingredient ingredient = new Ingredient(ingredientName, 1);

                // Sequence asynchronous calls to add and relate the recipe and ingrdient.
                Single<Long> addRecipe = recipeRepository.add(recipe);
                Single<Long> addIngredient = ingredientRepository.add(ingredient);
                Single<Integer> relateRecipeIngredient = addRecipe.concatMap(
                        recipeID -> addIngredient.concatMap(
                                ingredientID -> { // The lambda must return a `Single<>`, so make a garbage one.
                                    RecipeIngredientJoin relation = new RecipeIngredientJoin(recipeID, ingredientID);
                                    return recipeIngredientJoinRepository.insert(relation).toSingle(() -> {
                                       return 0;
                                    });
                                }
                ));

                // Execute the asynchronous calls we built up.
                relateRecipeIngredient.subscribeOn(Schedulers.io())
                        .subscribe((garbageInt) -> {
                            String message = "Recipe Name: " + recipeData.get("Recipe Name") + "\n" +
                                    "First Ingredient: " + recipeData.get("Ingredient");
                            Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                        });
            }
        });
    }
}
