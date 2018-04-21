package com.sabry.muhammed.qanda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sabry.muhammed.qanda.model.User;


public class MainActivity extends AppCompatActivity {
    public static User currentUser;

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    private Button emailButton;
    private Button facebookButton;
    private LoginButton realButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailButton = findViewById(R.id.email_login);
        facebookButton = findViewById(R.id.facebook_login);
        realButton = findViewById(R.id.actual_facebook_login);

        mAuth = FirebaseAuth.getInstance();


        callbackManager = CallbackManager.Factory.create();

        realButton.setReadPermissions("email");
        realButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realButton.performClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //User not inside
        if (currentUser != null) {
            displayQuestions(currentUser);
        }
    }

    private void displayQuestions(FirebaseUser user) {
        currentUser = new User();
        currentUser.setId(user.getUid());
        currentUser.setName(user.getDisplayName());
        currentUser.setPhotoUrl(user.getPhotoUrl().toString());
        Intent intent = new Intent(this, QuestionsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            Toast.makeText(MainActivity.this, "Welcome back " + user.getDisplayName() + " :)",
                                    Toast.LENGTH_SHORT).show();
                            displayQuestions(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
