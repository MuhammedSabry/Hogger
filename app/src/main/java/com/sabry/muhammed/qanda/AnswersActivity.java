package com.sabry.muhammed.qanda;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sabry.muhammed.qanda.adapter.AnswersRecyclerAdapter;
import com.sabry.muhammed.qanda.dialog.DialogFragment;
import com.sabry.muhammed.qanda.model.Answer;
import com.sabry.muhammed.qanda.model.Question;
import com.sabry.muhammed.qanda.util.CommonUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AnswersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView userName;
    private TextView questionText;
    private CircleImageView userPhoto;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        userName = findViewById(R.id.answer_user_name);
        questionText = findViewById(R.id.answer_question);
        userPhoto = findViewById(R.id.answer_user_photo);
        addButton = findViewById(R.id.add_answer_button);

        final Question question = (Question) getIntent().getExtras().get("question");

        questionText.setText(question.getQuestion());
        userName.setText(question.getUser().getName());
        CommonUtils.loadImage(userPhoto, question.getUser().getPhotoUrl());


        mRecyclerView = findViewById(R.id.answers_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if (question.getAnswers() == null)
            mAdapter = new AnswersRecyclerAdapter(new ArrayList<Answer>());
        else
            mAdapter = new AnswersRecyclerAdapter(question.getAnswers());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", question);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "Answer");
            }
        });
    }
}
