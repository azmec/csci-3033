package com.example.cookbook.ui.home;

import android.app.AlertDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.cookbook.R;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.example.cookbook.database.recipe.Recipe; // Replace with your actual Recipe class import

public class RecipeDetailDialogFragment extends DialogFragment {

    private static final String ARG_RECIPE = "recipe";
    private Recipe recipe;

    public static RecipeDetailDialogFragment newInstance(Recipe recipe) {
        RecipeDetailDialogFragment fragment = new RecipeDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recipe_popup, null);

        TextView recipeName = view.findViewById(R.id.popup_recipe_name);
        TextView recipeDescription = view.findViewById(R.id.popup_recipe_description);

        recipeName.setText(recipe.name);
        recipeDescription.setText(recipe.description);

        builder.setView(view)
                .setPositiveButton("Close", (dialog, id) -> {
                    // User closes the dialog
                });
        return builder.create();
    }
}
