package com.sabry.muhammed.qanda.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sabry.muhammed.qanda.R;
import com.sabry.muhammed.qanda.model.Answer;
import com.sabry.muhammed.qanda.util.CommonUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersRecyclerAdapter extends RecyclerView.Adapter<AnswersRecyclerAdapter.AnswersViewHolder> {
    List<Answer> answers;

    public AnswersRecyclerAdapter(List<Answer> answerList) {
        answers = answerList;
    }


    @NonNull
    @Override
    public AnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.answers_recycler_item, parent, false);
        return new AnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersViewHolder holder, int position) {
        holder.bind(answers.get(position));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class AnswersViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView userName;
        private TextView answerText;

        AnswersViewHolder(View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.answer_answer_user_name);
            this.userPhoto = itemView.findViewById(R.id.answer_answer_user_photo);
            this.answerText = itemView.findViewById(R.id.answer_answer);
        }

        void bind(Answer answer) {
            this.userName.setText(answer.getUser().getName());
            this.answerText.setText(answer.getAnswer());
            CommonUtils.loadImage(this.userPhoto, answer.getUser().getPhotoUrl());
        }
    }

}
