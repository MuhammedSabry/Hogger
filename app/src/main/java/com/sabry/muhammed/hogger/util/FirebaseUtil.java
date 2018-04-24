package com.sabry.muhammed.hogger.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sabry.muhammed.hogger.activity.MainActivity;
import com.sabry.muhammed.hogger.model.User;

public class FirebaseUtil {
    private static FirebaseAuth firebaseAuth;
    private static User currentUser = null;
    private static CallbackManager callbackManager;


    static {
        callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();
        setUser(firebaseAuth.getCurrentUser());
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static CallbackManager getCallBackManager() {
        return callbackManager;
    }

    public static void signInWithFacebook(Activity activity, AccessToken token, OnCompleteListener<AuthResult> completeListener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, completeListener);
    }

    public static void setUser(FirebaseUser user) {
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                    currentUser = null;
            }
        });
        if (user != null) {
            currentUser = new User();
            if (user.getDisplayName() == null)
                currentUser.setName("Anonymous");
            else
                currentUser.setName(user.getDisplayName());
            if (user.getDisplayName() == null)
                currentUser.setPhotoUrl("");
            else
                currentUser.setPhotoUrl(String.valueOf(user.getPhotoUrl()));
            currentUser.setId(user.getUid());
        }
    }

    public static void logoutUser(Context context) {
        LoginManager.getInstance().logOut();
        firebaseAuth.signOut();
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
