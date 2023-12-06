package org.csci.mealmanual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment; // Import Fragment class

import org.csci.mealmanual.R;

import org.csci.mealmanual.database.RecipeDatabase;
import org.csci.mealmanual.ui.home.RecipeFragment; // Import your fragment classes
import org.csci.mealmanual.ui.home.GroceryFragment;
import org.csci.mealmanual.ui.home.PantryFragment;
import org.csci.mealmanual.ui.home.LikedFragment;
import org.csci.mealmanual.ui.home.AddFragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.csci.mealmanual.databinding.ActivityMainBinding;
import org.csci.mealmanual.ui.home.RecipeViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    RecipeFragment recipeFragment;
    GroceryFragment groceryFragment;
    PantryFragment pantryFragment;
    LikedFragment likedFragment;
    AddFragment addFragment;

    private RecipeViewModel recipeViewModel;

    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the application-wide view-models.
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        FragmentManager fragmentManager = getSupportFragmentManager();
        int fragmentContainerID = R.id.nav_host_fragment_content_main;

        this.recipeFragment = new RecipeFragment();
        this.groceryFragment = new GroceryFragment();
        this.pantryFragment = new PantryFragment();
        this.likedFragment = new LikedFragment();
        this.addFragment = new AddFragment();

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_recipe_list);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null; // Use the generic Fragment type
            int id = item.getItemId();

            if (id == R.id.action_recipe_list) {
                selectedFragment = recipeFragment;
            } else if (id == R.id.action_grocery_list) {
                selectedFragment = groceryFragment;
            } else if (id == R.id.action_pantry_list) {
                selectedFragment = pantryFragment;
            } else if (id == R.id.action_liked_recipes) {
                selectedFragment = likedFragment;
            } else if (id == R.id.action_add_recipes) {
                selectedFragment = addFragment;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(fragmentContainerID, selectedFragment);
                transaction.setReorderingAllowed(true);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            return true;
        });


        // Load the default fragment
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(fragmentContainerID,  recipeFragment).commit();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cuisine)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(fragmentContainerID);
        NavController navController = navHostFragment.getNavController();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecipeDatabase.getInstance(getApplicationContext()).close();
    }
}
