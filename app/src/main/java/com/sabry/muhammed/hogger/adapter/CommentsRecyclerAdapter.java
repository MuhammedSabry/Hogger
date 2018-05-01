package com.sabry.muhammed.hogger.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.model.Comment;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.AnswersViewHolder> {
    private ArrayList<User> users;
    private LinkedHashMap<User, Comment> comments;

    public CommentsRecyclerAdapter(LinkedHashMap<User, Comment> commentList) {
        comments = commentList;
    }


    @NonNull
    @Override
    public AnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.comments_recycler_item, parent, false);
        return new AnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersViewHolder holder, int position) {
        this.users = new ArrayList<>(comments.keySet());
        Comment comment = comments.get(users.get(position));
        holder.bind(comment, users.get(position));
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
            this.userName = itemView.findViewById(R.id.comments_comment_user_name);
            this.userPhoto = itemView.findViewById(R.id.comments_comment_user_photo);
            this.answerText = itemView.findViewById(R.id.answer_answer);
        }

        void bind(Comment comment, User user) {
            this.userName.setText(user.getName());
            this.answerText.setText(comment.getComment());
            if (user.getPhotoUrl() == null)
                this.userPhoto.setImageResource(R.drawable.ic_no_photo);
            else
                CommonUtil.loadImage(this.userPhoto, user.getPhotoUrl());
        }
    }

}
