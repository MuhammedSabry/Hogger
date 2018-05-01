package com.sabry.muhammed.hogger.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.adapter.CommentsRecyclerAdapter;
import com.sabry.muhammed.hogger.model.Comment;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;

import java.util.LinkedHashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sabry.muhammed.hogger.util.CommonUtil.buttonLiked;
import static com.sabry.muhammed.hogger.util.CommonUtil.buttonNotLiked;
import static com.sabry.muhammed.hogger.util.FirebaseUtil.getCurrentUser;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.COMMENT;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.POST;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.USER;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.addLike;


public class CommentsActivity extends AppCompatActivity {


    private LinkedHashMap<User, Comment> commentsMap = new LinkedHashMap<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView userName;
    private TextView postText;
    private CircleImageView userPhoto;

    private Button commentButton;
    private Button likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentButton = findViewById(R.id.comment_button);

        userName = findViewById(R.id.post_user_name);
        postText = findViewById(R.id.post);
        userPhoto = findViewById(R.id.post_user_photo);
        likeButton = findViewById(R.id.like_button);

        final Post post = (Post) getIntent().getExtras().get(POST);
        final User user = (User) getIntent().getExtras().get(USER);

        postText.setText(post.getPost());
        userName.setText(user.getName());
        CommonUtil.loadImage(userPhoto, user.getPhotoUrl());
        if (post.getLikedUsers().get(user.getId()) != null && post.getLikedUsers().get(user.getId()))
            buttonLiked(likeButton);
        else
            buttonNotLiked(likeButton);

        mRecyclerView = findViewById(R.id.answers_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CommentsRecyclerAdapter(commentsMap);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                final View view = LayoutInflater.from(CommentsActivity.this).inflate(R.layout.dialog_fragment, null, false);
                builder.setView(view).setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView text = view.findViewById(R.id.question_text);
                        FirestoreUtil.addComment(text.getText().toString(), getCurrentUser(), post, CommentsActivity.this);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("Add Comment");
                builder.create().show();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLike(post, likeButton, user.getId());
            }
        });
        onDocumentChange(post);
    }


    private void onDocumentChange(Post post) {
        FirebaseFirestore.getInstance().collection(COMMENT).whereEqualTo("postId", post.getId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<DocumentChange> documentChanges = snapshots.getDocumentChanges();
                        for (int i = 0; i < documentChanges.size(); i++) {
                            DocumentChange documentChange = documentChanges.get(i);
                            switch (documentChange.getType()) {
                                case ADDED:
                                    loadCommnetUser(documentChange.getDocument().toObject(Comment.class));
                                    break;
                                case MODIFIED:
//                            postsList.set(i, documentChange.getDocument().toObject(Post.class));
                                    break;
                                case REMOVED:
//                            postsList.remove(i);
                                    break;
                            }
                        }
//                setPosts(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });


    }

    private void loadCommnetUser(final Comment comment) {
        FirebaseFirestore.getInstance().collection(USER).document(comment.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        commentsMap.put(task.getResult().toObject(User.class), comment);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
