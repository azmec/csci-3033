package org.csci.mealmanual.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.csci.mealmanual.R;
import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private List<String> groceryList; // Replace String with your Grocery model if you have one

    public GroceryAdapter(List<String> groceryList) {
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        String grocery = groceryList.get(position); // Replace String with your Grocery model if you have one
        holder.groceryName.setText(grocery);
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        CheckBox groceryCheckbox;
        TextView groceryName;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            groceryCheckbox = itemView.findViewById(R.id.groceryCheckbox);
            groceryName = itemView.findViewById(R.id.groceryName);
        }
    }
}
