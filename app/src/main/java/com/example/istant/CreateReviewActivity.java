package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.istant.model.Activity;
import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateReviewActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Activity activity;

    private RatingBar ratingBar;
    private EditText textInputEditText;
    private Button createReview;

    private String activityId;
    private int score;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        activity = getIntent().getParcelableExtra("activity");
        activityId = activity.getId();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ratingBar = findViewById(R.id.rev_rating);
        textInputEditText = findViewById(R.id.editTextreview);
        createReview = findViewById(R.id.btn_create_rev);

        score = ratingBar.getNumStars();
        comment = textInputEditText.getText().toString();

        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreActivityUserWrite(score, comment, activityId, auth.getCurrentUser().getUid(), db);

                ratingBar.setRating(0F);
                textInputEditText.getText().clear();
            }
        });

    }

    public static void scoreActivityUserWrite(int score, String comment,
                                              String idActivity, String uid, FirebaseFirestore db) {
        Map<String, Object> scoreActivityUser = new HashMap<>();

        scoreActivityUser.put("score", score);
        scoreActivityUser.put("comment", comment);
        scoreActivityUser.put("uid", uid);
        scoreActivityUser.put("activityid", idActivity);
        String id = SupportFunctions.generateRandomString();

        db.collection("scoreActivityUser").document(id)
                .set(scoreActivityUser)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }
}