package com.sabry.muhammed.qanda.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sabry.muhammed.qanda.R;
import com.sabry.muhammed.qanda.model.Question;

import static com.sabry.muhammed.qanda.MainActivity.currentUser;


public class QuestionDialogFragment extends DialogFragment {
    Context context;
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_question, null);
        editText = view.findViewById(R.id.question_text);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_question, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Question question = new Question();
                        question.setUser(currentUser);
                        question.setQuestion(editText.getText().toString());
                        addQuestion(question);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuestionDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }

    private void addQuestion(final Question question) {

        final DocumentReference document = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.questions_collection)).document();
        document.set(question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        question.setId(document.getId());
                        document.update("name", question.getId());
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
