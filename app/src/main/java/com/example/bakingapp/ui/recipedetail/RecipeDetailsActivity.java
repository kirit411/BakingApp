package com.example.bakingapp.ui.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.data.local.model.Recipe;
import com.example.bakingapp.data.local.model.Step;
import com.example.bakingapp.ui.stepdetail.StepDetailActivity;
import com.example.bakingapp.ui.stepdetail.StepDetailFragment;
import com.example.bakingapp.utils.ActivityUtils;
import com.example.bakingapp.utils.Constants;
import com.example.bakingapp.utils.Injection;
import com.example.bakingapp.utils.ViewModelFactory;
import com.example.bakingapp.widget.RecipeWidgetProvider;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_DATA = "extra_recipe";

    private Recipe recipe;

    private boolean mTwoPane = false;


    private RecipeDetailViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = getIntent().getParcelableExtra(EXTRA_RECIPE_DATA);
        if (recipe == null) {
            closeOnError();
            return;
        }

        // determine which layout we are in (tablet or phone)
        if (findViewById(R.id.fragment_step_detail) != null) {
            mTwoPane = true;
        }

        mViewModel = obtainViewModel(this);
        setupToolbar();
        if (savedInstanceState == null) {
            mViewModel.init(recipe, mTwoPane);
            setupViewFragment();
            saveRecipeDataToSharedPreferences(recipe);
            refreshWidgetIngredientsList(recipe);
        }
        // observe steps list click event
        mViewModel.getOpenStepDetailEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer position) {
                Step step = mViewModel.getCurrentStep().getValue();
                if (mTwoPane) {
                    StepDetailFragment fragment = StepDetailFragment.newInstance(step);
                    ActivityUtils.replaceFragmentInActivity(
                            getSupportFragmentManager(), fragment, R.id.fragment_step_detail);
                } else {
                    ArrayList<Step> steps = new ArrayList<>(recipe.getSteps());
                    Intent intent = new Intent(RecipeDetailsActivity.this, StepDetailActivity.class);
                    intent.putParcelableArrayListExtra(StepDetailActivity.EXTRA_STEP_LIST, steps);
                    intent.putExtra(StepDetailActivity.EXTRA_POSITION, position);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(recipe.getName());
        }
    }

    private void refreshWidgetIngredientsList(Recipe recipe) {
        // refresh ingredients list
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
        int ids[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(getApplication(), RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);

        // partially update widget recipe name so we don't have to recreate widget layout each time
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_recipe_ingredients);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipe.getName());
        appWidgetManager.partiallyUpdateAppWidget(ids, remoteViews);
    }

    private void saveRecipeDataToSharedPreferences(Recipe recipe) {
        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(getString(R.string.recipe_name), recipe.getName());
        editor.putLong(getString(R.string.recipe_id), recipe.getId());
        editor.apply();
    }

    private void setupViewFragment() {
        if (mTwoPane) {
            return;
        }
//        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance();
//        ActivityUtils.replaceFragmentInActivity(
//                getSupportFragmentManager(), recipeDetailFragment, R.id.fragment_recipe_detail);
    }

    public static RecipeDetailViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(RecipeDetailViewModel.class);
    }

    private void closeOnError() {
        throw new IllegalArgumentException("Access denied.");
    }
}
