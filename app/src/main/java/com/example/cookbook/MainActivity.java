package com.example.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment; // Import Fragment class
import com.example.cookbook.ui.home.RecipeFragment; // Import your fragment classes
import com.example.cookbook.ui.home.GroceryFragment;
import com.example.cookbook.ui.home.PantryFragment;
import com.example.cookbook.ui.home.LikedFragment;
import com.example.cookbook.ui.home.AddFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;


import com.example.cookbook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null; // Use the generic Fragment type
            int id = item.getItemId();

            if (id == R.id.action_recipe_list) {
                selectedFragment = new RecipeFragment();
            } else if (id == R.id.action_grocery_list) {
                selectedFragment = new GroceryFragment();
            } else if (id == R.id.action_pantry_list) {
                selectedFragment = new PantryFragment();
            } else if (id == R.id.action_liked_recipes) {
                selectedFragment = new LikedFragment();
            } else if (id == R.id.action_add_recipes) {
                selectedFragment = new AddFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, selectedFragment).commit();
            }

            return true;
        });


        // Load the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,  new RecipeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.action_recipe_list);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cuisine)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Remove the mail icon
        MenuItem item = menu.findItem(R.id.action_settings);
        if (item != null) item.setVisible(false);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
