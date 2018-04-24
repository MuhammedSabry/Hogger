package com.sabry.muhammed.hogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.sabry.muhammed.hogger.model.User;
import com.sabry.muhammed.hogger.util.FirebaseUtil;
import com.sabry.muhammed.hogger.util.FirestoreUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.sabry.muhammed.hogger.util.FirebaseUtil.getCurrentUser;


public class MainActivity extends AppCompatActivity {

    private Button emailButton;
    private Button facebookButton;
    private LoginButton realButton;

    private Button loginButton;
    private EditText emailText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHash(this);
        emailButton = findViewById(R.id.email_login);
        facebookButton = findViewById(R.id.facebook_login);
        realButton = findViewById(R.id.actual_facebook_login);

        loginButton = findViewById(R.id.login);
        emailText = findViewById(R.id.emailField);
        passwordText = findViewById(R.id.passwordField);

        //Facebook login handling
        realButton.setReadPermissions("email");
        realButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        realButton.registerCallback(FirebaseUtil.getCallBackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                FirebaseUtil.signInWithFacebook(MainActivity.this, loginResult.getAccessToken(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Welcome message
                            FirebaseUtil.setUser(task.getResult().getUser());
                            Toast.makeText(MainActivity.this, "Welcome back " + getCurrentUser().getName() + " :)", Toast.LENGTH_SHORT).show();
                            launchActivity();
                        } else {
                            realButton.setEnabled(true);
                            emailButton.setEnabled(true);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });


        //Just to perform the click on the real hidden Facebook Login Button
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookButton.setEnabled(false);
                emailButton.setEnabled(false);
                realButton.performClick();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EmailRegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailText.getText() != null && passwordText.getText() != null) {
                    String email = emailText.getText().toString();
                    String password = passwordText.getText().toString();
                    if (!email.isEmpty() && !password.isEmpty()) {
                        FirebaseUtil.loginUser(email, password, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUtil.setUser(task.getResult().getUser());
                                    launchActivity();
                                } else {
                                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    manager.hideSoftInputFromWindow(loginButton.getRootView().getWindowToken(), 0);
                                    Snackbar.make(loginButton.getRootView(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void launchActivity() {
        FirestoreUtil.addUser(getCurrentUser());
        startActivity(new Intent(this, PostsActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        realButton.setEnabled(true);
        emailButton.setEnabled(true);
        User user = getCurrentUser();
        //User not inside
        if (user != null) {
            launchActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseUtil.getCallBackManager().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void getHash(Activity activity) {
        PackageInfo info;

        try {
            info = activity.getPackageManager().getPackageInfo("com.sabry.muhammed.hogger", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
