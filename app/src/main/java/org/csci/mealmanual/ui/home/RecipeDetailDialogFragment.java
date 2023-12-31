package org.csci.mealmanual.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.csci.mealmanual.R;
import org.csci.mealmanual.database.business.DomainRecipe;
import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Tag;

import java.util.List;

public class RecipeDetailDialogFragment extends DialogFragment {
    private static final String ARG_RECIPE = "recipe";
    private DomainRecipe recipe;
    private ImageButton likeButton;
    private RecipeViewModel recipeViewModel;

    private Fragment parentFragment;

    private boolean alreadyLiked;
    private boolean firstClick;

    public static RecipeDetailDialogFragment newInstance(DomainRecipe recipe) {
        RecipeDetailDialogFragment fragment = new RecipeDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Initialize the view model.
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alreadyLiked = false;
        firstClick = true;

        if (getArguments() != null) {
            recipe = (DomainRecipe) getArguments().getSerializable(ARG_RECIPE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recipe_popup, null);

        // Configure the recipe's labels.
        configureTextViews(view);

        // If the button is clicked, add the recipe to the "Liked" recipes.
        likeButton.setOnClickListener(this::handleLikeEvent);

        // If the user presses the button, dismiss the dialogue.
        builder.setView(view).setPositiveButton("Close", (dialog, id) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    /**
     * Configure the labels corresponding to the recipe's values.
     * @param view The `View` the `TextView` objects are in.
     */
    private void configureTextViews(View view) {
        // Retrieve references to the text views.
        TextView recipeName = view.findViewById(R.id.popup_recipe_name);
        TextView recipeDescription = view.findViewById(R.id.popup_recipe_description);
        TextView recipeIngredients = view.findViewById(R.id.popup_recipe_ingredients);
        TextView recipeTagsLabel = view.findViewById(R.id.popup_recipe_tags);
        likeButton = view.findViewById(R.id.button_like_recipe);

        // Create a commented list for tags.
        StringBuilder recipeTagString = new StringBuilder();
        List<Tag> recipeTagList = recipe.getTags();
        int numTags = recipeTagList.size();
        if (numTags > 0) {
            recipeTagString.append("Tags: ");
            for (int i = 0; i < numTags - 1; i++) {
                Tag tag = recipeTagList.get(i);
                recipeTagString.append(tag.tag);
                recipeTagString.append(", ");
            }
            recipeTagString.append(recipeTagList.get(numTags - 1).tag);
        } else {
            recipeTagString.append("No tags are associated.");
        }

        // Create a commented list for ingredients.
        StringBuilder recipeIngredientString = new StringBuilder();
        List<Ingredient> recipeIngredientList = recipe.getIngredients();
        int numIngredients = recipeIngredientList.size();
        if (numIngredients > 0) {
            recipeIngredientString.append("Ingredients: ");
            for (int i = 0; i < numIngredients - 1; i++) {
                Ingredient ingredient = recipeIngredientList.get(i);
                recipeIngredientString.append(ingredient.name);
                recipeIngredientString.append(", ");
            }
            recipeIngredientString.append(recipeIngredientList.get(numIngredients - 1).name);
        } else {
            recipeIngredientString.append("No ingredients are associated.");
        }

        // Set the text values for the `TextView`s.
        recipeName.setText(recipe.getName());
        recipeDescription.setText(Html.fromHtml(recipe.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        recipeIngredients.setText(recipeIngredientString);
        recipeTagsLabel.setText(recipeTagString);
    }

    /**
     * Handle the case where the "like" button is pressed.
     * @param view The `View` the like button is in.
     */
    private void handleLikeEvent(View view) {
        List<Tag> recipeTags = recipe.getTags();

        // Determine if this recipe has the like tag in-memory. Because we cannot immediately update
        // whether this recipe has the tag in-memory, we flip a flag to determine whether we should
        // like it.
        for (Tag tag : recipeTags) {
            if (tag.uid == RecipeDatabase.LIKED_TAG.uid && firstClick) { // First interaction, so unlike.
                this.recipeViewModel.removeLike(recipe);
                Snackbar.make(view, "Removed Favorite", Snackbar.LENGTH_SHORT).show();

                firstClick = false;
                return;
            } else if (tag.uid == RecipeDatabase.LIKED_TAG.uid) { // Already interacted with, so toggle the like.
                if (alreadyLiked) {
                    this.recipeViewModel.removeLike(recipe);
                    alreadyLiked = false;

                    Snackbar.make(view, "Removed Favorite", Snackbar.LENGTH_SHORT).show();
                } else {
                    this.recipeViewModel.likeRecipe(recipe);
                    alreadyLiked = true;
                    Snackbar.make(view, "Added Favorite", Snackbar.LENGTH_SHORT).show();
                }

                return;
            }
        }

        // It is possible we've already liked this recipe w/o closing the dialogue. This means
        // the in-memory `domainRecipe`'s tags do not reflect those in the database, so the
        // the above check will fail. This internal boolean will prevent us from double-liking
        // the recipe in this case.
        if (alreadyLiked) {
            this.recipeViewModel.removeLike(recipe);
            alreadyLiked = false;

            Snackbar.make(view, "Removed Favorite", Snackbar.LENGTH_SHORT).show();
        } else {
            this.recipeViewModel.likeRecipe(recipe);
            alreadyLiked = true;
            Snackbar.make(view, "Added Favorite", Snackbar.LENGTH_SHORT).show();
        }

        firstClick = false;
    }

    public void setParentFragment(Fragment fragment) {
        this.parentFragment = fragment;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (this.parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
    }
}
