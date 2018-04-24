package com.sabry.muhammed.hogger.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sabry.muhammed.hogger.CommentsActivity;
import com.sabry.muhammed.hogger.model.Comment;
import com.sabry.muhammed.hogger.model.Post;
import com.sabry.muhammed.hogger.model.User;

import java.util.Date;
import java.util.List;

public class FirestoreUtil {

    public static String POST = "Post";
    public static String COMMENT = "Comment";
    public static String USER = "User";
    public static String DATE = "date";

    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;

    static {
        db = FirebaseFirestore.getInstance();
    }

    public static void getPosts() {
        db.collection(POST)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Post> posts = queryDocumentSnapshots.toObjects(Post.class);
//                                    PostsActivity.setPosts(posts);
                                }
                            });
                        }
                    }
                });
    }

    public static void getComments(Post post) {
        db.collection(COMMENT).whereEqualTo("postId", post.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Comment> comments = queryDocumentSnapshots.toObjects(Comment.class);
//                                    CommentsActivity.setComments(comments);
                                }
                            });
                        }
                    }
                });
    }

    public static User getUser(String id) {
        final User[] user = {new User()};
        db.collection(USER).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user[0] = documentSnapshot.toObject(User.class);
            }
        });
        return user[0];
    }

    public static void post(String text, User user, final Context context) {
        DocumentReference document = db.collection(POST).document();
        Post post = new Post();
        post.setPost(text);
        post.setDate(new Date());
        post.setId(document.getId());
        post.setUserId(user.getId());
        document.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Post added :)", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Post not added :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void addComment(String s, User currentUser, Post post,
                                  final CommentsActivity commentsActivity) {
        DocumentReference document = db.collection(COMMENT).document();
        Comment comment = new Comment();
        comment.setComment(s);
        comment.setDate(new Date());
        comment.setId(document.getId());
        comment.setUserId(currentUser.getId());
        comment.setPostId(post.getId());
        document.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(commentsActivity, "Comment added :)", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(commentsActivity, "Comment not added :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void addUser(User currentUser) {
        DocumentReference document = db.collection(USER).document(currentUser.getId());
        document.set(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }
}
