package com.example.bakingapp.ui.recipedetail.steps;

import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.data.local.model.Step;
import com.example.bakingapp.ui.recipedetail.RecipeDetailViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> mStepList;

    private RecipeDetailViewModel viewModel;

    public StepsAdapter(RecipeDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StepViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Step step = mStepList.get(position);
        ((StepViewHolder) holder).bindTo(step, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentStep(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStepList != null ? mStepList.size() : 0;
    }

    public void submitList(List<Step> steps) {
        mStepList = steps;
        notifyDataSetChanged();
    }
}
