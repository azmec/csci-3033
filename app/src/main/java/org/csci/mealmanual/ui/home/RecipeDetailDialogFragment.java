package org.csci.mealmanual.ui.home;

import android.app.AlertDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import org.csci.mealmanual.R;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import org.csci.mealmanual.database.DomainRecipe;

public class RecipeDetailDialogFragment extends DialogFragment {

    private static final String ARG_RECIPE = "recipe";
    private DomainRecipe recipe;

    public static RecipeDetailDialogFragment newInstance(DomainRecipe recipe) {
        RecipeDetailDialogFragment fragment = new RecipeDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            recipe = (DomainRecipe) getArguments().getSerializable(ARG_RECIPE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recipe_popup, null);

        TextView recipeName = view.findViewById(R.id.popup_recipe_name);
        TextView recipeDescription = view.findViewById(R.id.popup_recipe_description);

        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());

        builder.setView(view)
                .setPositiveButton("Close", (dialog, id) -> {
                    // User closes the dialog
                });
        return builder.create();
    }
}
