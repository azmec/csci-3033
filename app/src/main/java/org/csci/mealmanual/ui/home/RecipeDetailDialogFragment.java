package org.csci.mealmanual.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import org.csci.mealmanual.R;
import org.csci.mealmanual.database.DomainRecipe;
import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.database.model.Tag;

import java.util.List;

public class RecipeDetailDialogFragment extends DialogFragment {
    private static final String ARG_RECIPE = "recipe";
    private DomainRecipe recipe;
    private ImageButton likeButton;
    private RecipeViewModel recipeViewModel;

    public static RecipeDetailDialogFragment newInstance(DomainRecipe recipe) {
        RecipeDetailDialogFragment fragment = new RecipeDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        // Initialize the view model.
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.initRepository(context);


        if (getArguments() != null) {
            recipe = (DomainRecipe) getArguments().getSerializable(ARG_RECIPE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recipe_popup, null);

        TextView recipeName = view.findViewById(R.id.popup_recipe_name);
        TextView recipeDescription = view.findViewById(R.id.popup_recipe_description);
        likeButton = view.findViewById(R.id.button_like_recipe);

        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());

        // User closes the dialog
        builder.setView(view).setPositiveButton("Close", (dialog, id) -> {});

        // If the button is clicked, add the recipe to the "Liked" recipes.
        likeButton.setOnClickListener(v -> {
            // If the recipe is already liked, we do not need to add it. So, exit this lambda.
            // TODO: Cache whether this is liked or not in the recipe itself.
            List<Tag> recipeTags = recipe.getTags();
            for (Tag tag : recipeTags) {
                if (tag.uid == RecipeDatabase.LIKED_TAG.uid) {
                    return;
                }
            }

            this.recipeViewModel.likeRecipe(recipe);
        });

        return builder.create();
    }
}
