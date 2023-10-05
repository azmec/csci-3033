package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {
    private Button AddButton;
    private List<String> RecipeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        AddButton = (Button) findViewById(R.id.buttonSubmit);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipe();
            }
        });
    }
    //This method will add what the user inputs to the existing data structure
    public void addRecipe() {
        //This will prompt the user to enter something in the text field
        String input = ""; //Feed the user's input into this
        RecipeList.add(input); //Append what is entered into the existing data structure
    }
}
