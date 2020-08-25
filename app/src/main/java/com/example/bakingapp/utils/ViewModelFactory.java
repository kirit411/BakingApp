package com.example.bakingapp.utils;

import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.ui.recipedetail.RecipeDetailViewModel;
import com.example.bakingapp.ui.recipelist.RecipeListViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RecipeRepository repository;

    public static ViewModelFactory getInstance(RecipeRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(RecipeRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeListViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeListViewModel(repository);
        } else if (modelClass.isAssignableFrom(RecipeDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeDetailViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
