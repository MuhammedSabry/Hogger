package com.sabry.muhammed.qanda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sabry.muhammed.qanda.adapter.AnswersRecyclerAdapter;
import com.sabry.muhammed.qanda.model.Question;

public class AnswersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Question question = (Question) getIntent().getExtras().get("question");

        TextView viewById = findViewById(R.id.answer_question);
        viewById.setText(question.getQuestion());


        mRecyclerView = findViewById(R.id.questions_recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new AnswersRecyclerAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

    }
}
