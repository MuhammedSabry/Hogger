package com.sabry.muhammed.hogger.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sabry.muhammed.hogger.util.CommonUtil.buttonLiked;
import static com.sabry.muhammed.hogger.util.CommonUtil.buttonNotLiked;


public class PostsRecyclerAdapter extends android.support.v7.widget.RecyclerView.Adapter<PostsRecyclerAdapter.QuestionsViewHolder> {

    private View mainView;
    private ArrayList<User> users;
    private Map<User, Post> list;
    private OnItemClickListener mOnClick;

    public PostsRecyclerAdapter(Map<User, Post> posts, OnItemClickListener listener) {
        this.list = posts;
        this.users = new ArrayList<>(list.keySet());
        this.mOnClick = listener;
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mainView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.posts_recycler_item
                        , parent
                        , false);
        return new QuestionsViewHolder(mainView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position) {
        this.users = new ArrayList<>(list.keySet());
        Post post = list.get(users.get(position));
        holder.bind(post, users.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void OnClick(View view, User user, int position);

        boolean OnLongClickListener(View view, int position);
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView postText;
        TextView userName;
        CircleImageView userPhoto;

        Button likeButton;
        Button commentButton;

        QuestionsViewHolder(View view) {
            super(view);
            this.postText = view.findViewById(R.id.post);
            this.userName = view.findViewById(R.id.post_user_name);
            this.userPhoto = view.findViewById(R.id.post_user_photo);
            this.likeButton = view.findViewById(R.id.like_button);
            this.commentButton = view.findViewById(R.id.comment_button);

            likeButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        //Binding data to views inside the recyclerView item
        void bind(Post q, User user) {
            postText.setText(q.getPost());
            CommonUtil.loadImage(this.userPhoto, user.getPhotoUrl());
            this.userName.setText(user.getName());

            if (q.getLikedUsers().get(user.getId()) != null && q.getLikedUsers().get(user.getId()))
                buttonLiked(likeButton);
            else
                buttonNotLiked(likeButton);

        }

        //Implementing onClick interface
        @Override
        public void onClick(View v) {
            if (getAdapterPosition() >= 0)
                mOnClick.OnClick(v, users.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
