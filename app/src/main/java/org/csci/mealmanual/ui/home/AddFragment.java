package org.csci.mealmanual.ui.home;

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

import org.csci.mealmanual.R;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.RecipeIngredientJoin;
import org.csci.mealmanual.database.repo.IngredientRepository;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.repo.RecipeIngredientJoinRepository;
import org.csci.mealmanual.database.repo.RecipeRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
        EditText quantityEditText = view.findViewById(R.id.editTextQuantity);
        EditText recipeDescriptionEditText = view.findViewById(R.id.editTextRecipeDescription);
        EditText tagEditText = view.findViewById(R.id.editTextTag);
        LinearLayout ingredientsLayout = view.findViewById(R.id.ingredientsLayout);
        LinearLayout tagsLayout = view.findViewById(R.id.tagsLayout);
        Button addIngredientButton = view.findViewById(R.id.buttonAddIngredient);
        Button addTagButton = view.findViewById(R.id.buttonAddTag);
        Button submitButton = view.findViewById(R.id.buttonSubmit);
        List<EditText> listTag = new ArrayList<EditText>();
        listTag.add(tagEditText);
        List<EditText> listIngredientName = new ArrayList<EditText>();
        List<EditText> listQuantity = new ArrayList<EditText>();
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newTagEditText = new EditText(getContext());
                newTagEditText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                newTagEditText.setHint("Tag");
                newTagEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                // Add the new EditText to the ingredientsLayout
                listTag.add(newTagEditText);
                tagsLayout.addView(newTagEditText);
            }
        });

        //Handle the click of the "Add Ingredient" button
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limit to 15 ingredients
                if (ingredientsLayout.getChildCount() < 100) {
                    // Create a new horizontal layout to put new buttons on
                    LinearLayout newLinearLayoutHorizontal = new LinearLayout(getContext());
                    newLinearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                    EditText newQuantityEditText = new EditText(getContext());
                    newQuantityEditText.setLayoutParams(new ViewGroup.LayoutParams(
                            ingredientsLayout.getWidth()/2,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    newQuantityEditText.setHint("Quantity");
                    newQuantityEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Add the new EditText to the ingredientsLayout
                    newLinearLayoutHorizontal.addView(newQuantityEditText);

                    EditText newIngredientEditText = new EditText(getContext());
                    newIngredientEditText.setLayoutParams(new ViewGroup.LayoutParams(
                            ingredientsLayout.getWidth()/2,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    newIngredientEditText.setHint("Ingredient");
                    newIngredientEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Add the new EditText to the ingredientsLayout
                    newLinearLayoutHorizontal.addView(newIngredientEditText);

                    ingredientsLayout.addView(newLinearLayoutHorizontal);
                    listQuantity.add(newQuantityEditText);
                    listIngredientName.add(newIngredientEditText);

                } else {
                    // Inform the user about the limit
                    Snackbar.make(v, "Maximum 100 ingredients allowed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Handle the click of the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean listEmpty = false;
                for (int j = 0; j < listIngredientName.size(); j++) {
                    if((ingredientEditText.getText().toString().trim().length() == 0) || (quantityEditText.getText().toString().trim().length() == 0)) {
                        listEmpty = true;
                    }
                }

                if((recipeNameEditText.getText().toString().trim().length() == 0) || (recipeDescriptionEditText.getText().toString().trim().length() == 0) || listEmpty) {
                    String message = "Please fill out all information for recipe";
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                } else {
                    // Create a HashMap to store the recipe data
                    HashMap<String, String> recipeData = new HashMap<>();
                    recipeData.put("Recipe Name", recipeNameEditText.getText().toString());
                    List<Ingredient> ingredientList = new ArrayList<>();

                    // Create recipe and send to the repository
                    String name = recipeNameEditText.getText().toString();
                    String description = recipeDescriptionEditText.getText().toString();
                    String ingredientName = ingredientEditText.getText().toString();
                    int ingredientQuantity = Integer.parseInt(quantityEditText.getText().toString());

                    Recipe recipe = new Recipe(name, description);
                    Ingredient ingredient = new Ingredient(ingredientName, ingredientQuantity);
                    ingredientList.add(ingredient);

                    for (int j = 0; j < listIngredientName.size(); j++) {
                        String ingredientName2 = ingredientEditText.getText().toString();
                        int ingredientQuantity2 = Integer.parseInt(quantityEditText.getText().toString());
                        Ingredient ingredient2 = new Ingredient(ingredientName, ingredientQuantity);
                        ingredientList.add(ingredient2);
                    }

                    // Sequence asynchronous calls to add and relate the recipe and ingredients.
                    Single<Long> addRecipe = recipeRepository.add(recipe);
                    Single<List<Long>> addIngredient = ingredientRepository.add(ingredientList.toArray(new Ingredient[0]));
                    Single<Integer> relateRecipeIngredient = addRecipe.concatMap(
                            recipeID -> addIngredient.concatMap(
                                    ingredientIDs -> { // The lambda must return a `Single<>`, so make a garbage one.
                                        ArrayList<RecipeIngredientJoin> relations = new ArrayList<>();
                                        for (long ingredientID : ingredientIDs) {
                                            relations.add(new RecipeIngredientJoin(recipeID, ingredientID));
                                        }

                                        return recipeIngredientJoinRepository.insert(relations.toArray(new RecipeIngredientJoin[0])).toSingle(() -> {
                                            return 0;
                                        });
                                    }
                            ));

                    // Execute the asynchronous calls we built up.
                    relateRecipeIngredient.subscribeOn(Schedulers.io())
                            .subscribe((garbageInt) -> {
                                String message = "Recipe Name: " + recipeData.get("Recipe Name");
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                            });
                }
            }
        });
    }
}
