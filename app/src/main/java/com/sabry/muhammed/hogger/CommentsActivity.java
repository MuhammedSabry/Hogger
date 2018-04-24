package com.sabry.muhammed.hogger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sabry.muhammed.hogger.adapter.CommentsRecyclerAdapter;
import com.sabry.muhammed.hogger.model.Comment;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.CommonUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sabry.muhammed.hogger.util.FirebaseUtil.getCurrentUser;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.COMMENT;
import static com.sabry.muhammed.hogger.util.FirestoreUtil.POST;


public class CommentsActivity extends AppCompatActivity {

    private List<Comment> commentsList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView userName;
    private TextView questionText;
    private CircleImageView userPhoto;

    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        floatingActionButton = findViewById(R.id.add_answer_button);

        userName = findViewById(R.id.answer_user_name);
        questionText = findViewById(R.id.answer_question);
        userPhoto = findViewById(R.id.answer_user_photo);

        final Post post = (Post) getIntent().getExtras().get(POST);
        User user = FirestoreUtil.getUser(post.getUserId());

        questionText.setText(post.getPost());
        userName.setText(user.getName());
        CommonUtil.loadImage(userPhoto, user.getPhotoUrl());


        mRecyclerView = findViewById(R.id.answers_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CommentsRecyclerAdapter(commentsList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                final View view = LayoutInflater.from(CommentsActivity.this).inflate(R.layout.dialog_fragment, null, false);
                builder.setView(view).setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                });
                builder.create().show();
            }
        });

//        FirestoreUtil.getComments(post);
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
                                    commentsList.add(documentChange.getDocument().toObject(Comment.class));
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
