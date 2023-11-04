package com.example.cookbook.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookbook.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GroceryFragment extends Fragment {
    GroceryViewModel groceryViewModel;
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
    private RecyclerView groceryRecyclerView;
    private SearchView grocerySearchView;
    private MaterialButton addGroceryButton;
    private MaterialButton finalizeAddGroceryButton;
    private MaterialButton pantryTransferButton;
    private Spinner categorySpinner;  // New Spinner for category selection

    private GroceryAdapter groceryAdapter;
    private List<String> groceryList; // Replace String with your Grocery model if you have one

    public GroceryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groceryViewModel = new ViewModelProvider(requireActivity()).get(GroceryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_fragment, container, false);
        EditText editGroceryName = view.findViewById(R.id.editTextGroceryName);
        LinearLayout addGroceriesLayout = view.findViewById(R.id.addGroceriesLayout);
        categorySpinner = view.findViewById(R.id.categorySpinner);  // Initialize Spinner
        //grocerySearchView = view.findViewById(R.id.grocerySearchView);
        groceryRecyclerView = view.findViewById(R.id.groceryRecyclerView);
        addGroceryButton = view.findViewById(R.id.buttonAddGroceries);
        finalizeAddGroceryButton = view.findViewById(R.id.buttonFinalizeAddGroceries);
        pantryTransferButton = view.findViewById(R.id.buttonTransferToPantry);

        // Initialize grocery list
        groceryList = new ArrayList<>(); // Populate this list with your data
        groceryList = groceryViewModel.getData();

        // Setup RecyclerView and Adapter
        groceryAdapter = new GroceryAdapter(groceryList);
        groceryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groceryRecyclerView.setAdapter(groceryAdapter);

        // Initialize Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.grocery_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Filter grocery list by selected category
                // You can call a function to update the RecyclerView based on the selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Setup SearchView
        /*
        grocerySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change
                return false;
            }
        });
        */
        // Setup FloatingActionButton
        addGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add grocery
                // For example, open a dialog to add a new grocery item
                addGroceriesLayout.setVisibility(View.VISIBLE);

            }
        });
        finalizeAddGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the grocery from EditText
                String grocery = editGroceryName.getText().toString();
                //Add to GroceryList list
                groceryList.add(grocery);
                //Update the view model
                groceryViewModel.setData(groceryList);

                //Add the grocery to the recycler view and then disable the visibility
                editGroceryName.setText("");
                addGroceriesLayout.setVisibility(View.GONE);
            }
        });
        pantryTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Import the pantry view model to be updated
                IngredientViewModel ingredientViewModel =  new ViewModelProvider(requireActivity()).get(IngredientViewModel.class);
                //Updated pantry with groceries
                List<String> newPantry = ingredientViewModel.getData();
                //Groceries to be transferred
                List<String> toBeTransferred = groceryViewModel.getData();

                //Transfer groceries to the pantry
                for(int i = 0; i < toBeTransferred.size(); i++){
                    newPantry.add(toBeTransferred.get(i));
                }

                //Update the pantry to include these groceries
                ingredientViewModel.setData(newPantry);
            }
        });
        //
        return view;

    }
}
