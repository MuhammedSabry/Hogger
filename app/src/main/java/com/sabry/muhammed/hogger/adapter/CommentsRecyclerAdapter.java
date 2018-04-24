package com.sabry.muhammed.hogger.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.model.Comment;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.AnswersViewHolder> {
    List<Comment> comments;

    public CommentsRecyclerAdapter(List<Comment> commentList) {
        comments = commentList;
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
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
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

        void bind(Comment comment) {
            this.userName.setText(comment.getUserId());
            this.answerText.setText(comment.getComment());
            FirestoreUtil.getUser(comment.getId()).getPhotoUrl();
            CommonUtil.loadImage(this.userPhoto, FirestoreUtil.getUser(comment.getId()).getPhotoUrl());
        }
    }

}
