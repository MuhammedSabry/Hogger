package com.sabry.muhammed.hogger.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sabry.muhammed.hogger.R;
import com.sabry.muhammed.hogger.adapter.PostsRecyclerAdapter;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static com.sabry.muhammed.hogger.util.CommonUtil.roundImage;
import static com.sabry.muhammed.hogger.util.CommonUtil.shutDownActivity;
import static com.sabry.muhammed.hogger.util.FirebaseUtil.getCurrentUser;
import static com.sabry.muhammed.hogger.util.FirebaseUtil.logoutUser;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.POST;


public class PostsActivity extends AppCompatActivity {

    private static RecyclerView.Adapter mAdapter;
    private List<Post> postsList;
    private Target target;

    private RecyclerView mRecyclerView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        postsList = new ArrayList<>();

        floatingActionButton = findViewById(R.id.add_question_button);

        //RecyclerView parameters initialization
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new PostsRecyclerAdapter(postsList, new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Post post = postsList.get(position);
                if (view.getId() == R.id.question_bookmark) {
                    //TODO put logic for checking weather a post is starred or not
                    Log.d("PostsActivity", "Post marked as favorite");
                }

                //Send user to comments section
                openCommentsActivity(post);
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostsActivity.this);
                final View view = LayoutInflater.from(PostsActivity.this).inflate(R.layout.dialog_fragment, null, false);
                builder.setView(view).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView text = view.findViewById(R.id.question_text);
                        FirestoreUtil.post(text.getText().toString(), getCurrentUser(), PostsActivity.this);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        onDocumentChange();
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

    private void openCommentsActivity(Post post) {
        Intent intent = new Intent(PostsActivity.this, CommentsActivity.class);
        intent.putExtra(POST, post);
        startActivity(intent);
    }

    private void onDocumentChange() {
        FirebaseFirestore.getInstance().collection(POST)
                .orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            postsList.add(documentChange.getDocument().toObject(Post.class));
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
}
