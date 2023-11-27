package org.csci.mealmanual.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.csci.mealmanual.R;
import org.csci.mealmanual.database.DomainRecipe;

import java.util.ArrayList;

public class LikedFragment extends Fragment implements RecipeAdapter.ItemClickListener, DialogInterface.OnDismissListener {
    private ArrayList<DomainRecipe> likedList;
    private RecipeViewModel recipeViewModel;
    private RecipeAdapter recipeAdapter;
    private RecyclerView recyclerView;
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
    public LikedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();

        // Initialize the view model.
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.initRepository(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liked_fragment, container, false);

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.likedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the ViewModel and observe liked recipes
        recipeViewModel.getLikedRecipeData().observe(getViewLifecycleOwner(), likedRecipes -> {
            // Update the adapter when data changes
            likedList = new ArrayList<>(likedRecipes);
            recipeAdapter = new RecipeAdapter(getContext(), likedList);
            recipeAdapter.setClickListener(this);
            recyclerView.setAdapter(recipeAdapter);
        });

        return view;
    }

    public void onItemClick(View view, DomainRecipe recipe) {
        // Handle the click event here
        RecipeDetailDialogFragment dialogFragment = RecipeDetailDialogFragment.newInstance(recipe);
        dialogFragment.setParentFragment(this);
        dialogFragment.show(getParentFragmentManager(), "recipe_details");
    }

    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        this.recipeViewModel.updateLikedRecipeData();
    }
}
