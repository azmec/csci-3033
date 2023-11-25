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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.csci.mealmanual.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //AppCompatDelegate appCompatDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RecipeDatabase.getInstance(getApplicationContext()).close();
    }
}
