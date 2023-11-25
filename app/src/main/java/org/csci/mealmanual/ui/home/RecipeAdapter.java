package org.csci.mealmanual.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import org.csci.mealmanual.R;
import org.csci.mealmanual.database.model.Recipe;
import org.csci.mealmanual.database.model.Tag;
import org.csci.mealmanual.database.repo.RecipeRepository;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final List<RecipeRepository.RecipeWithTag> recipeList;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecipeAdapter(Context context, List<RecipeRepository.RecipeWithTag> recipeList) {
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
        RecipeRepository.RecipeWithTag recipeWithTag = recipeList.get(position);
        Recipe recipe = recipeWithTag.recipe;
        List<Tag> tags = recipeWithTag.tags; // TODO: Do something with this
        holder.recipeName.setText(recipe.name);
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
    RecipeRepository.RecipeWithTag getItem(int id) {
        return recipeList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Recipe recipe);
    }
}

