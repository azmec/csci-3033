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
        ImageButton cameraButton = view.findViewById(R.id.cameraAddButton);
        List<EditText> listTag = new ArrayList<EditText>();
        List<EditText> listIngredientName = new ArrayList<EditText>();
        List<EditText> listQuantity = new ArrayList<EditText>();
        List<LinearLayout> listLayoutIngredients = new ArrayList<LinearLayout>();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraOrCameraRollPopup();
            }
        });
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
                    // Create a new horizontal layout to put new buttons on
                    LinearLayout newLinearLayoutHorizontal = new LinearLayout(getContext());
                    newLinearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    listLayoutIngredients.add(newLinearLayoutHorizontal);
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
            }
        });

        //Handle the click of the "Submit" button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean listEmpty = false;
                for (int j = 0; j < listIngredientName.size(); j++) {
                    if((listIngredientName.get(j).getText().toString().trim().length() == 0) || (listQuantity.get(j).getText().toString().trim().length() == 0)) {
                        listEmpty = true;
                    }
                }

                for (int j = 0; j < listTag.size(); j++) {
                    if(listTag.get(j).getText().toString().trim().length() == 0) {
                        listEmpty = true;
                    }
                }
                if((recipeNameEditText.getText().toString().trim().length() == 0) || (tagEditText.getText().toString().trim().length() == 0) || (recipeDescriptionEditText.getText().toString().trim().length() == 0) || (ingredientEditText.getText().toString().trim().length() == 0) || (quantityEditText.getText().toString().trim().length() == 0)|| listEmpty) {
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
                    String ingredientUnit = ""; // TODO: Receive from the user!
                    int ingredientQuantity = Integer.parseInt(quantityEditText.getText().toString());

                    Recipe recipe = new Recipe(name, description);

                    if(imageRecipe != null) {
                        String imageRecipeString = imageRecipe.toString();
                        recipe.setImageUrl(imageRecipeString);
                    }

                    Ingredient ingredient = new Ingredient(ingredientName, ingredientUnit, ingredientQuantity);
                    ingredientList.add(ingredient);

                    for (int j = 0; j < listIngredientName.size(); j++) {
                        String ingredientName2 = ingredientEditText.getText().toString();
                        int ingredientQuantity2 = Integer.parseInt(quantityEditText.getText().toString());
                        Ingredient ingredient2 = new Ingredient(ingredientName, ingredientUnit, ingredientQuantity);
                        ingredientList.add(ingredient2);
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
                    Single<List<Long>> addIngredient = ingredientRepository.add(ingredientList.toArray(new Ingredient[0]));
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
                                String message = "Recipe Name: " + recipeData.get("Recipe Name");
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                            });

                    for (int j = 0; j < listIngredientName.size(); j++) {
                        ingredientsLayout.removeView(listLayoutIngredients.get(j));
                    }

                    for (int j = 0; j < listTag.size(); j++) {
                        tagsLayout.removeView(listTag.get(j));
                    }
                    recipeNameEditText.setText("");
                    recipeDescriptionEditText.setText("");
                    ingredientEditText.setText("");
                    quantityEditText.setText("");
                    tagEditText.setText("");
                }
            }
        });
    }
    private void chooseCameraOrCameraRollPopup() {
        String[] options = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose an option");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    takePictureIntent();
                    break;
                case 1:
                    cameraRollIntent();
                    break;
            }
        });

        builder.show();
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void cameraRollIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
    }

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
