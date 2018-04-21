package com.sabry.muhammed.qanda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sabry.muhammed.qanda.adapter.QuestionsRecyclerAdapter;
import com.sabry.muhammed.qanda.dialog.QuestionDialogFragment;
import com.sabry.muhammed.qanda.model.Question;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static com.sabry.muhammed.qanda.MainActivity.currentUser;
import static com.sabry.muhammed.qanda.util.CommonUtils.roundImage;
import static com.sabry.muhammed.qanda.util.CommonUtils.shutDownActivity;


public class QuestionsActivity extends AppCompatActivity {

    private List<Question> questionList = new ArrayList<>();
    private Target target;
    private FirebaseFirestore db;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton;
    private CollectionReference questionsCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        setTitle("Questions");

        db = FirebaseFirestore.getInstance();
        questionsCollection = db.collection(getResources().getString(R.string.questions_collection));

        floatingActionButton = findViewById(R.id.floatingActionButton);

        mRecyclerView = findViewById(R.id.questions_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new QuestionsRecyclerAdapter(questionList, new QuestionsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Question question = questionList.get(position);
                if (view.getId() == R.id.question_bookmark) {
                    //TODO put logic for checking weather a question is starred or not
                }
                Intent intent = new Intent(QuestionsActivity.this, AnswersActivity.class);
                intent.putExtra("context", question);
            }

            @Override
            public boolean OnLongClickListener(View view, int position) {
                return false;
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDialogFragment dialog = new QuestionDialogFragment();
                dialog.show(getFragmentManager(), "QuestionAdder");
            }
        });

        questionsCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   task.getResult().getQuery().addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                           if (e != null) {
                                                               return;
                                                           }
                                                           for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                                               switch (dc.getType()) {
                                                                   case ADDED:
                                                                       questionList.add(dc.getDocument().toObject(Question.class));
                                                                       mAdapter.notifyDataSetChanged();
                                                                       break;
                                                                   case MODIFIED:
                                                                       break;
                                                                   case REMOVED:
                                                                       questionList.remove(dc.getDocument().toObject(Question.class));
                                                                       break;
                                                               }
                                                           }

                                                       }
                                                   });
                                               } else {
                                                   //TODO add handler to re-query data later FirebaseJobScheduler
                                                   Toast.makeText(QuestionsActivity.this, "failed to get questions :(", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }
                );

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.questions_menu, menu);
        menu.getItem(0).setTitle(currentUser.getName());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                menu.getItem(0).setIcon(roundImage(bitmap, QuestionsActivity.this));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.get().load(currentUser.getPhotoUrl()).into(target);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_icon:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        shutDownActivity(this);
    }
}
