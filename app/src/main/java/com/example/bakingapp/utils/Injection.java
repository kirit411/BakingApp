package com.example.bakingapp.utils;

import android.content.Context;

import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.data.local.RecipesDatabase;
import com.example.bakingapp.data.remote.ApiClient;
import com.example.bakingapp.data.remote.RecipeService;


public class Injection {
    public static ViewModelFactory provideViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context);
        return ViewModelFactory.getInstance(repository);
    }

    public static RecipeRepository provideRecipeRepository(Context context) {
        RecipeService apiService = ApiClient.getInstance();
        AppExecutors executors = AppExecutors.getInstance();
        RecipesDatabase database = RecipesDatabase.getInstance(context.getApplicationContext());
        return RecipeRepository.getInstance(
                executors,
                apiService,
                database);
    }
}
