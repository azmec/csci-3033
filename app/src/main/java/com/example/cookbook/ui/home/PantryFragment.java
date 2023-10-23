package com.example.cookbook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cookbook.R;

public class PantryFragment extends Fragment {

    public PantryFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.pantry_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout currentIngredientsLayout = view.findViewById(R.id.currentIngredientsLayout);
        LinearLayout addIngredientsLayout = view.findViewById(R.id.addIngredientsLayout);
        Button buttonAddToPantry = view.findViewById(R.id.buttonAddToPantry);
        Button buttonViewPantry = view.findViewById(R.id.buttonViewPantry);
        Button buttonSaveIngredient = view.findViewById(R.id.buttonSaveIngredient);

        buttonAddToPantry.setOnClickListener(v -> {
            currentIngredientsLayout.setVisibility(View.GONE);
            addIngredientsLayout.setVisibility(View.VISIBLE);
        });

        buttonViewPantry.setOnClickListener(v -> {
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });

        buttonSaveIngredient.setOnClickListener(v -> {
            // Retrieve data from EditText
            // Save to data source
            // Update the currentIngredientsLayout with new ingredient
            // Switch back to viewing mode
            currentIngredientsLayout.setVisibility(View.VISIBLE);
            addIngredientsLayout.setVisibility(View.GONE);
        });
    }
}
