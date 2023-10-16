package com.example.cookbook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GroceryFragment extends Fragment {

    private RecyclerView groceryRecyclerView;
    private SearchView grocerySearchView;
    private FloatingActionButton addGroceryButton;
    private Spinner categorySpinner;  // New Spinner for category selection
    private GroceryAdapter groceryAdapter;
    private List<String> groceryList; // Replace String with your Grocery model if you have one

    public GroceryFragment() {
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
        View view = inflater.inflate(R.layout.grocery_fragment, container, false);

        categorySpinner = view.findViewById(R.id.categorySpinner);  // Initialize Spinner
        grocerySearchView = view.findViewById(R.id.grocerySearchView);
        groceryRecyclerView = view.findViewById(R.id.groceryRecyclerView);
        //addGroceryButton = view.findViewById(R.id.addGroceryButton);

        // Initialize grocery list
        groceryList = new ArrayList<>(); // Populate this list with your data

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

        // Setup FloatingActionButton
//        addGroceryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle add grocery
//                // For example, open a dialog to add a new grocery item
//            }
//        });

        return view;
    }
}
