package com.pratapkumar.bakingapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.pratapkumar.bakingapplication.models.Ingredient;
import com.pratapkumar.bakingapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private List<Ingredient> mIngredients;
    private String mRecipeName;

    private final IngredientsAdapterOnClickHandler mOnClickHandler;

    public interface IngredientsAdapterOnClickHandler {
        void onIngredientClick(int ingredientIndex);
    }

    public IngredientsAdapter(List<Ingredient> ingredients, IngredientsAdapterOnClickHandler onClickHandler,
                              String recipeName) {
        mIngredients = ingredients;
        mOnClickHandler = onClickHandler;
        mRecipeName = recipeName;
    }


    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item,parent,false);
        return new IngredientsAdapter.IngredientsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        //holder.bind(ingredient, position);

        holder.tvQuantity.setText(String.valueOf(ingredient.getmQuantity()));
        holder.tvMeasure.setText(ingredient.getmMeasure());
        holder.tvIngredient.setText(ingredient.getmIngredient());
    }


    @Override
    public int getItemCount() {
        if (null == mIngredients) return 0;
        return mIngredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Nullable
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        @Nullable
        @BindView(R.id.tv_measure)
        TextView tvMeasure;

        @Nullable
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;

         IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickHandler.onIngredientClick(adapterPosition);
        }
    }
}
