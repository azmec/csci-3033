package org.csci.mealmanual.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import org.csci.mealmanual.R;
import org.csci.mealmanual.database.DomainRecipe;
import org.csci.mealmanual.database.model.Ingredient;
import org.csci.mealmanual.database.model.Tag;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final List<DomainRecipe> recipeList;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecipeAdapter(Context context, List<DomainRecipe> recipeList) {
        this.mInflater = LayoutInflater.from(context);
        this.recipeList = recipeList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DomainRecipe recipe = recipeList.get(position);

        // TODO: Display these pl0x thank you glhf
        List<Tag> tags = recipe.getTags();
        List<Ingredient> ingredients = recipe.getIngredients();
        Log.d("YES", "onBindViewHolder: " + tags);
        Log.d("YES", "onBindViewHolder: " + ingredients);

        holder.recipeName.setText(recipe.getName());
        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) mClickListener.onItemClick(v, recipe);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;

        ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
        }
    }

    // convenience method for getting data at click position
    DomainRecipe getItem(int id) {
        return recipeList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, DomainRecipe recipe);
    }
}

