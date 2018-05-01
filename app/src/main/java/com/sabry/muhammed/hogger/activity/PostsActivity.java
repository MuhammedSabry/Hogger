package com.sabry.muhammed.hogger.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import com.sabry.muhammed.hogger.adapter.PostsRecyclerAdapter;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sabry.muhammed.hogger.util.CommonUtil.roundImage;
import static com.sabry.muhammed.hogger.util.CommonUtil.shutDownActivity;
import static com.sabry.muhammed.hogger.util.FirebaseUtil.getCurrentUser;
import static com.sabry.muhammed.hogger.util.FirebaseUtil.logoutUser;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.POST;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.USER;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.addLike;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.post;


public class PostsActivity extends AppCompatActivity {

    private Map<User, Post> postsMap = new HashMap<>();

    private RecyclerView.Adapter mAdapter;
    private Target target;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button postButton;
    private TextInputEditText postTextEditText;
    private CircleImageView postUserPhoto;
    private TextView postUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);


        postButton = findViewById(R.id.add_post_button);
        postTextEditText = findViewById(R.id.post_text);
        postUserName = findViewById(R.id.post_user_name);
        postUserPhoto = findViewById(R.id.post_user_photo);

        CommonUtil.loadImage(postUserPhoto, getCurrentUser().getPhotoUrl());
        postUserName.setText(getCurrentUser().getName());

        //RecyclerView parameters initialization
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new PostsRecyclerAdapter(postsMap, new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, User user, int position) {
                switch (view.getId()) {

                    //Add like or remove like
                    case R.id.like_button:
                        Post post = postsMap.get(user);
                        addLike(post, position, mAdapter, getCurrentUser().getId());
                        break;

                    //Open the post comments
                    default:
                        //Send user to comments section
                        openCommentsActivity(postsMap.get(user), user);
                        break;
                }
            }

            @Override
            public boolean OnLongClickListener(View view, int position) {
                return false;
            }
        });

        //RecyclerView setup
        mRecyclerView = findViewById(R.id.posts_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        onDocumentChange();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!postTextEditText.getText().toString().isEmpty()) {
                    post(postTextEditText.getText().toString(), getCurrentUser(), PostsActivity.this);
                    postTextEditText.setText("");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.questions_menu, menu);
        menu.getItem(0).setTitle(getCurrentUser().getName());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {

        if (!getCurrentUser().getPhotoUrl().isEmpty()) {
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    menu.getItem(0).setIcon(roundImage(bitmap, PostsActivity.this));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

            CommonUtil.loadImage(target, getCurrentUser().getPhotoUrl());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_icon:
                logoutUser(this);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getCurrentUser() == null)
            startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        shutDownActivity(this);
    }

    private void openCommentsActivity(Post post, User user) {
        Intent intent = new Intent(PostsActivity.this, CommentsActivity.class);
        intent.putExtra(POST, post);
        intent.putExtra(USER, user);
        startActivity(intent);
    }

    private void onDocumentChange() {
        FirebaseFirestore.getInstance().collection(POST)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<DocumentChange> documentChanges = snapshots.getDocumentChanges();
                        for (DocumentChange documentChange : documentChanges) {
                            Post post = documentChange.getDocument().toObject(Post.class);
                            switch (documentChange.getType()) {
                                case ADDED:
                                    loadPostUser(post);
                                    break;
                                case MODIFIED:
//                            loadPostUser(post);
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadPostUser(final Post post) {
        FirebaseFirestore.getInstance().collection(USER).document(post.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        postsMap.put(snapshot.toObject(User.class), post);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
