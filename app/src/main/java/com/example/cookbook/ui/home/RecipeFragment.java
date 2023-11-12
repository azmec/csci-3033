package com.example.cookbook.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookbook.R;
import com.example.cookbook.database.model.Recipe;
import com.example.cookbook.database.repo.RecipeRepository;

import java.util.ArrayList;

public class RecipeFragment extends Fragment implements RecipeAdapter.ItemClickListener {

    private RecipeViewModel recipeViewModel;
    private RecipeRepository recipeRepository;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();

        // Initialize the view model.
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.initRepository(context);

        // Initialize our repository.
        recipeRepository = new RecipeRepository(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        recyclerView = view.findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            recipeList = new ArrayList<>(recipes);
            recipeAdapter = new RecipeAdapter(getContext(), recipeList);
            recipeAdapter.setClickListener(this); // Set the click listener
            recyclerView.setAdapter(recipeAdapter);
        });

        return view;
    }

    @Override
    public void onItemClick(View view, Recipe recipe) {
        // Handle the click event here
        RecipeDetailDialogFragment dialogFragment = RecipeDetailDialogFragment.newInstance(recipe);
        dialogFragment.show(getParentFragmentManager(), "recipe_details");
    }
}
