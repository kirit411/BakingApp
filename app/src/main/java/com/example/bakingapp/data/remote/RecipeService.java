package com.example.bakingapp.data.remote;

import com.example.bakingapp.data.local.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Udacity recipes REST API access points.
 *
 *

 */
public interface RecipeService {

    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();
}
