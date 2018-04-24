package com.sabry.muhammed.hogger.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostsRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter<PostsRecyclerAdapter.QuestionsViewHolder> {

    private List<Post> list;
    private OnItemClickListener mOnClick;

    public PostsRecyclerAdapter(List<Post> qList, OnItemClickListener listener) {
        this.list = qList;
        this.mOnClick = listener;
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.posts_recycler_item
                        , parent
                        , false);
        return new QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position) {
        Post post = list.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void OnClick(View view, int position);

        boolean OnLongClickListener(View view, int position);
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder {

        TextView postText;
        TextView topComment;
        ConstraintLayout layout;
        TextView userName;
        CircleImageView userPhoto;

        QuestionsViewHolder(View view) {
            super(view);
            this.postText = view.findViewById(R.id.post);
            this.topComment = view.findViewById(R.id.first_answer);
            this.layout = view.findViewById(R.id.item);
            this.userName = view.findViewById(R.id.question_user_name);
            this.userPhoto = view.findViewById(R.id.question_user_photo);

            //Implementing onClick interface
            this.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClick.OnClick(v, getAdapterPosition());
                }
            });
            //Implementing onLongClick interface
            this.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnClick.OnLongClickListener(v, getAdapterPosition());
                }
            });
        }

        //Binding data to views inside the recyclerView item
        void bind(Post q) {
            User user = FirestoreUtil.getUser(q.getUserId());
            postText.setText(q.getPost());
            CommonUtil.loadImage(this.userPhoto, user.getPhotoUrl());
            this.userName.setText(user.getName());
            topComment.setText("comments here");
        }
    }
}
