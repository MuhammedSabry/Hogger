package com.sabry.muhammed.qanda.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AnswersRecyclerAdapter extends RecyclerView.Adapter<AnswersRecyclerAdapter.AnswersViewHolder> {


    @NonNull
    @Override
    public AnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AnswersViewHolder extends RecyclerView.ViewHolder {

        public AnswersViewHolder(View itemView) {
            super(itemView);
        }
    }
}
