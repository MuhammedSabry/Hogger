package com.sabry.muhammed.hogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.sabry.muhammed.hogger.util.FirebaseUtil.getFirebaseAuth;

public class EmailRegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;

    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        name = findViewById(R.id.register_name_field);
        email = findViewById(R.id.register_email_field);
        password = findViewById(R.id.register_password_field);
        register = findViewById(R.id.register_button);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText() != null && email.getText() != null && password.getText() != null) {
                    if (!name.getText().toString().isEmpty()) {
                        getFirebaseAuth()
                                .createUserWithEmailAndPassword(email.getText().toString()
                                        , password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(EmailRegisterActivity.this, MainActivity.class));
                                        } else {
                                            Snackbar.make(register.getRootView().getRootView(), "Wrong email or password", Snackbar.LENGTH_SHORT);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}
