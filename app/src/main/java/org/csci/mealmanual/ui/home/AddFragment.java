package org.csci.mealmanual.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContentResolver;
import android.content.ContentValues;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.csci.mealmanual.R;

import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.RecipeIngredientJoin;
import org.csci.mealmanual.database.model.RecipeTagJoin;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.repo.IngredientRepository;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.repo.RecipeIngredientJoinRepository;
import org.csci.mealmanual.database.repo.RecipeRepository;
import org.csci.mealmanual.database.repo.RecipeTagJoinRepository;
import org.csci.mealmanual.database.repo.TagRepository;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.app.AlertDialog;

public class AddFragment extends Fragment {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final TagRepository tagRepository;
    private final RecipeIngredientJoinRepository recipeIngredientJoinRepository;
    private final RecipeTagJoinRepository recipeTagJoinRepository;

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    Uri imageRecipe;
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
        this.tagRepository = new TagRepository(context);

        this.recipeIngredientJoinRepository = new RecipeIngredientJoinRepository(context);
        this.recipeTagJoinRepository = new RecipeTagJoinRepository(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Request permission from user to use camera
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            requestCameraPermission();
        } else {
            // Permission already granted, proceed with your logic
            // For example, initialize your camera here
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_fragment, container, false);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    // Check useer permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // Check if the permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your logic
                // For example, initialize your camera here
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable camera functionality)
            }
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        // Edit texts
        EditText recipeNameEditText = view.findViewById(R.id.editTextRecipeName);
        EditText ingredientEditText = view.findViewById(R.id.editTextIngredients);
        EditText quantityEditText = view.findViewById(R.id.editTextQuantity);
        EditText recipeDescriptionEditText = view.findViewById(R.id.editTextRecipeDescription);
        EditText tagEditText = view.findViewById(R.id.editTextTag);

        // Layouts
        LinearLayout ingredientsLayout = view.findViewById(R.id.ingredientsLayout);
        LinearLayout tagsLayout = view.findViewById(R.id.tagsLayout);

        // Buttons
        Button addIngredientButton = view.findViewById(R.id.buttonAddIngredient);
        Button addTagButton = view.findViewById(R.id.buttonAddTag);
        Button submitButton = view.findViewById(R.id.buttonSubmit);
        ImageButton cameraButton = view.findViewById(R.id.cameraAddButton);

        // Lists
        List<EditText> listTag = new ArrayList<EditText>();
        List<EditText> listIngredientName = new ArrayList<EditText>();
        List<EditText> listQuantity = new ArrayList<EditText>();
        List<LinearLayout> listLayoutIngredients = new ArrayList<LinearLayout>();

        // Camera button, which will call the choice prompt method
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraOrCameraRollPopup();
            }
        });

        // Add another tag to the recipe button
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new edit text for user input
                EditText newTagEditText = new EditText(getContext());
                newTagEditText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                // Label the edit text and set input type
                newTagEditText.setHint("Tag");
                newTagEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                // Add the new EditText to the list of tags to read later when saving info to recipe
                listTag.add(newTagEditText);

                // Add the new EditText to the ingredientsLayout to appear on UI
                tagsLayout.addView(newTagEditText);
            }
        });

        // Handle the click of the "Add Ingredient" button
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new horizontal layout in for quantity edit text and ingredient edit text
                LinearLayout newLinearLayoutHorizontal = new LinearLayout(getContext());
                newLinearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                // Add the horizontal layout to layout list, so we can later delete this when recipe successfully submits
                listLayoutIngredients.add(newLinearLayoutHorizontal);

                // Create a new edit text for user input for quantity
                EditText newQuantityEditText = new EditText(getContext());
                    newQuantityEditText.setLayoutParams(new ViewGroup.LayoutParams(
                            ingredientsLayout.getWidth()/2,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                // Label the edit text and set input type
                newQuantityEditText.setHint("Quantity");
                newQuantityEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                // Add the new EditText to the horizontal layout
                newLinearLayoutHorizontal.addView(newQuantityEditText);

                // Create a new edit text for user input for ingredient
                EditText newIngredientEditText = new EditText(getContext());
                newIngredientEditText.setLayoutParams(new ViewGroup.LayoutParams(
                        ingredientsLayout.getWidth()/2,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                // Label the edit text and set input type
                newIngredientEditText.setHint("Ingredient");
                newIngredientEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                // Add the new EditText to the horizontal layout
                newLinearLayoutHorizontal.addView(newIngredientEditText);

                // Add the horizontal layout containing the two edit texts to the ingredients layout so it shows on UI
                ingredientsLayout.addView(newLinearLayoutHorizontal);

                // Add the two edit texts to respective lists, to add to recipe information
                listQuantity.add(newQuantityEditText);
                listIngredientName.add(newIngredientEditText);
            }
        });

        //Handle the click of the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all information is filled out before submitting recipe to the repository

                // flag if any of the lists of tags, quantities, or ingredients is empty
                boolean listEmpty = false;

                // Loop to check if any value in ingredients or quantity are empty
                for (int j = 0; j < listIngredientName.size(); j++) {
                    if((listIngredientName.get(j).getText().toString().trim().length() == 0) || (listQuantity.get(j).getText().toString().trim().length() == 0)) {
                        listEmpty = true;
                    }
                }

                // Loop to check if any value in tags are empty
                for (int j = 0; j < listTag.size(); j++) {
                    if(listTag.get(j).getText().toString().trim().length() == 0) {
                        listEmpty = true;
                    }
                }

                // Check flag and if other fields are empty
                if((recipeNameEditText.getText().toString().trim().length() == 0) || (tagEditText.getText().toString().trim().length() == 0) || (recipeDescriptionEditText.getText().toString().trim().length() == 0) || (ingredientEditText.getText().toString().trim().length() == 0) || (quantityEditText.getText().toString().trim().length() == 0)|| listEmpty) {
                    // Print out message prompting user to fill everything out
                    String message = "Please fill out all information for recipe";
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                } else { // If user has everything filled out, proceed with storing information and sending recipe to the repository

                    // Get information from respective edit texts
                    String name = recipeNameEditText.getText().toString();
                    String description = recipeDescriptionEditText.getText().toString();

                    // Create an instance of recipe in order to send it to the repository
                    Recipe recipe = new Recipe(name, description);

                    // Images are optional; save image if necessary
                    if(imageRecipe != null) {
                        String imageRecipeString = imageRecipe.toString();
                        recipe.setImageUrl(imageRecipeString);
                    }

                    // Accumulate the user's ingredients.
                    ArrayList<Ingredient> recipeIngredients = new ArrayList<>();
                    String ingredientName1 = ingredientEditText.getText().toString();
                    String ingredientUnit1 = ""; // TODO: Receive from the user!
                    int ingredientQuantity1 = Integer.parseInt(quantityEditText.getText().toString());
                    Ingredient ingredient1 = new Ingredient(ingredientName1, ingredientUnit1, ingredientQuantity1);
                    recipeIngredients.add(ingredient1);
                    for (int j = 0; j < listIngredientName.size(); j++) {
                        String ingredientName2 = listIngredientName.get(j).getText().toString();
                        String ingredientUnit2 = ""; // TODO: Receive from the user!
                        int ingredientQuantity2 = Integer.parseInt(listQuantity.get(j).getText().toString());
                        Ingredient ingredient2 = new Ingredient(ingredientName2, ingredientUnit2, ingredientQuantity2);
                        recipeIngredients.add(ingredient2);
                    }

                    // Accumulate the user's tags.
                    ArrayList<Tag> recipeTags = new ArrayList<>();
                    String tagName1 = tagEditText.getText().toString();
                    Tag tag1 = new Tag(tagName1);
                    recipeTags.add(tag1);
                    for (EditText tagInput : listTag) {
                        String tagName = tagInput.getText().toString();
                        Tag tag = new Tag(tagName);
                        recipeTags.add(tag);
                    }

                    // Sequence asynchronous calls to add and relate the recipe and ingredients.
                    Single<Long> addRecipe = recipeRepository.add(recipe);
                    Single<List<Long>> addIngredient = ingredientRepository.add(recipeIngredients.toArray(new Ingredient[0]));
                    Single<List<Long>> addTags = tagRepository.add(recipeTags.toArray(new Tag[0]));
                    Completable completable = Single.zip(addRecipe, addTags, addIngredient, (recipeID, tagIDs, ingredientIDs) -> {
                        // Associate the added recipe with the tags.
                        ArrayList<RecipeTagJoin> tagRelations = new ArrayList<>();
                        for (long tagID : tagIDs)
                            tagRelations.add(new RecipeTagJoin(recipeID, tagID));
                        recipeTagJoinRepository.add(tagRelations.toArray(new RecipeTagJoin[0])).blockingSubscribe();

                        // Associate the added recipe with the ingredients.
                        ArrayList<RecipeIngredientJoin> ingredientRelations = new ArrayList<>();
                        for (long ingredientID : ingredientIDs)
                            ingredientRelations.add(new RecipeIngredientJoin(recipeID, ingredientID));
                        recipeIngredientJoinRepository.insert(ingredientRelations.toArray(new RecipeIngredientJoin[0])).blockingSubscribe();

                        return 0;
                    }).ignoreElement();

                    // Execute the asynchronous calls we built up.
                    completable.subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                String message = "Recipe Name: " + recipeNameEditText.getText().toString();
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                            });

                    // Clear everything out the fragment to make a clean slate for new recipes
                    // Remove all ingredient edit texts excluding the first one
                    for (int j = 0; j < listIngredientName.size(); j++) {
                        ingredientsLayout.removeView(listLayoutIngredients.get(j));
                    }

                    // Remove all tag edit texts excluding the first one
                    for (int j = 0; j < listTag.size(); j++) {
                        tagsLayout.removeView(listTag.get(j));
                    }

                    // Clear all default text boxes
                    recipeNameEditText.setText("");
                    recipeDescriptionEditText.setText("");
                    ingredientEditText.setText("");
                    quantityEditText.setText("");
                    tagEditText.setText("");
                    imageRecipe = null;
                }
            }
        });
    }

    // Method for dialog box to let user choose action
    private void chooseCameraOrCameraRollPopup() {
        String[] options = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose an option");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Call take picture method
                    takePictureIntent();
                    break;
                case 1:
                    // Call camera roll method
                    cameraRollIntent();
                    break;
            }
        });

        builder.show();
    }

    // Method to send action for taking picture
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    // Method to send action for opening camera roll
    private void cameraRollIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
    }

    // Method for action and saving data from the action
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    ContentResolver contentResolver = requireContext().getContentResolver();

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_" + System.currentTimeMillis() + ".jpg");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                    // Insert an empty image into the MediaStore, which will return a content URI
                    imageRecipe = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null && data.getData() != null) {
                    imageRecipe = data.getData();
                }
            }
        }
    }
}
