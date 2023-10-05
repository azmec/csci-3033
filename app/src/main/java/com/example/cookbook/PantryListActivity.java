package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {
    private Button AddButton;
    private List<String> pantryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list2);

        AddButton = (Button) findViewById(R.id.button);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Display UI for user input
                AddToPantryList();
            }
        });
    }

    //This method will add what the user inputs to the existing data structure
    public void AddToPantryList() {
        //This will prompt the user to enter something in the text field
        String input = ""; //Feed the user's input into this
        pantryList.add(input); //Append what is entered into the existing data structure
    }
}

