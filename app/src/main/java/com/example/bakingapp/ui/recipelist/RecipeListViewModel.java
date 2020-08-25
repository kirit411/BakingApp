package com.example.bakingapp.ui.recipelist;

import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.data.local.model.Recipe;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


public class RecipeListViewModel extends ViewModel {

    private LiveData<List<Recipe>> listLiveData;

    public RecipeListViewModel(RecipeRepository repository) {
        listLiveData = repository.loadAllRecipes();
    }

    public LiveData<List<Recipe>> getListLiveData() {
        return listLiveData;
    }
}
