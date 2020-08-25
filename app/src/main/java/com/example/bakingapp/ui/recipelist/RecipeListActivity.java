package com.example.bakingapp.ui.recipelist;

import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.data.local.model.Recipe;
import com.example.bakingapp.utils.EspressoIdlingResource;
import com.example.bakingapp.utils.Injection;
import com.example.bakingapp.utils.ViewModelFactory;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;


public class RecipeListActivity extends AppCompatActivity {

    private RecipeListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        viewModel = obtainViewModel();
        setupListAdapter();
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_recipe_list);
        final RecipesAdapter adapter = new RecipesAdapter();
        final GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_spans));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        EspressoIdlingResource.increment();
        // observe recipe list
        viewModel.getListLiveData().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    EspressoIdlingResource.decrement();
                    adapter.submitList(recipes);
                }
            }
        });
    }

    private RecipeListViewModel obtainViewModel() {
        ViewModelFactory factory = Injection.provideViewModelFactory(this);
        return ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
