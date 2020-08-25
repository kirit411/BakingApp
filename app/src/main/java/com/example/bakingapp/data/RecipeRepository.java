package com.example.bakingapp.data;

import com.example.bakingapp.data.local.RecipesDatabase;
import com.example.bakingapp.data.local.model.Ingredient;
import com.example.bakingapp.data.remote.RecipeService;
import com.example.bakingapp.data.local.model.Recipe;
import com.example.bakingapp.utils.AppExecutors;

import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class RecipeRepository {

    private static volatile RecipeRepository sInstance;
    private final RecipeService mRecipeService;
    private final RecipesDatabase mRecipesDatabase;
    private final AppExecutors mExecutors;

    private RecipeRepository(AppExecutors executors,
                             RecipeService recipeService,
                             RecipesDatabase database) {
        mExecutors = executors;
        mRecipeService = recipeService;
        mRecipesDatabase = database;
    }

    public static RecipeRepository getInstance(AppExecutors executors,
                                               RecipeService recipeService,
                                               RecipesDatabase database) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(executors, recipeService, database);
                }
            }
        }
        return sInstance;
    }

    public List<Ingredient> getAllIngredients() {
        return mRecipesDatabase.ingredientsDao().getAllIngredients();
    }

    public void saveRecipe(final Recipe recipe) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
//                mRecipesDatabase.recipesDao().insertRecipe(recipe);
                mRecipesDatabase.ingredientsDao().nukeIngredient();
                insertIngredients(recipe.getIngredients());
            }
        });
    }

    private void insertIngredients(List<Ingredient> ingredients) {
        mRecipesDatabase.ingredientsDao().insertAllIngredients(ingredients);
        Timber.d("%s ingredients inserted into database.", ingredients.size());
    }

    public LiveData<List<Recipe>> loadAllRecipes() {
        final MutableLiveData<List<Recipe>> recipeListLiveData = new MutableLiveData<>();
        mRecipeService.getAllRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> data = response.body();
                    List<Recipe> recipes = data != null ? data : Collections.<Recipe>emptyList();
                    Timber.d("Parsing finished. number of recipes: %s", recipes.size());
                    recipeListLiveData.postValue(recipes);
                } else {
                    // TODO:  handle errors
                    Timber.d("error code: %s", response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // TODO:  handle errors
                Timber.d("unknown error: %s", t.getMessage());
            }
        });
        return recipeListLiveData;
    }
}
