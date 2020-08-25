package com.example.bakingapp.ui.recipedetail.ingredients;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bakingapp.data.local.model.Ingredient;
import com.example.bakingapp.databinding.ItemIngredientBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class IngredientViewHolder extends RecyclerView.ViewHolder {

    private final ItemIngredientBinding binding;

    public IngredientViewHolder(@NonNull ItemIngredientBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindTo(final Ingredient ingredient) {
        binding.setIngredient(ingredient);

        binding.executePendingBindings();
    }

    public static IngredientViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemIngredientBinding binding =
                ItemIngredientBinding.inflate(layoutInflater, parent, false);
        return new IngredientViewHolder(binding);
    }
}
