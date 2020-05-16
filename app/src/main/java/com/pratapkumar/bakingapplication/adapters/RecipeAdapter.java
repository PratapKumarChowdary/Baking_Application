package com.pratapkumar.bakingapplication.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private Context context;
    private final RecipeClickListener listener;

    public RecipeAdapter(List<Recipe> recipes, Context context, RecipeClickListener listener) {
        this.recipes = recipes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipes.get(position),listener,position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.recipeImage)
        ImageView recipeImageView;
        @BindView(R.id.recipeName)
        TextView recipeNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final Recipe recipe, final RecipeClickListener recipeClickListener, int position)
        {
            recipeNameTextView.setText(recipe.getmName());

            int imageResourceId = Constant.getImageResource(position);
            recipeImageView.setImageResource(imageResourceId);

//            if(!recipe.getmImage().equals("") && recipe.getmImage() != null)
//            {
//                Picasso.get()
//                        .load(recipe.getmImage())
//                        .placeholder(R.drawable.cake)
//                        .into(recipeImageView);
//            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeClickListener.onRecipeItemClicked(recipe);
                }
            });

        }
    }


    public interface RecipeClickListener{
        void onRecipeItemClicked(Recipe recipe);
    }

}
