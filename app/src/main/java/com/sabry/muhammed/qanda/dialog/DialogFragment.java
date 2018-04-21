package com.sabry.muhammed.qanda.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sabry.muhammed.qanda.R;
import com.sabry.muhammed.qanda.model.Answer;
import com.sabry.muhammed.qanda.model.Question;

import java.util.Date;

import static com.sabry.muhammed.qanda.MainActivity.currentUser;


public class DialogFragment extends android.app.DialogFragment {
    Context context;
    private EditText editText;
    private Question question;
    private TextView textView;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        question = (Question) args.getSerializable("question");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_fragment, null);
        editText = view.findViewById(R.id.question_text);
        textView = view.findViewById(R.id.dialog_text);
        textView.setText(getTag());

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Add " + getTag(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        switch (getTag()) {
                            case "Answer":
                                addAnswer();
                                break;
                            case "Question":
                                addQuestion();
                                break;
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }

    private void addAnswer() {
        final Answer answer = new Answer();
        answer.setDate(new Date());
        answer.setQuestion(question.getId());
        answer.setUser(currentUser);
        answer.setAnswer(editText.getText().toString());
        answer.setId(question.getAnswers().size());
        question.getAnswers().add(answer);
        final DocumentReference document = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.questions_collection)).document(question.getId());
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                document.set(question).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "answer added :)", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        question.getAnswers().remove(answer);
                        Toast.makeText(context, "answer not added :(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addQuestion() {
        Question question = new Question();
        question.setUser(currentUser);
        question.setQuestion(editText.getText().toString());
        question.setDate(new Date());
        final DocumentReference document = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.questions_collection)).document();
        question.setId(document.getId());
        document.set(question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "question added :)", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "question not added :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
