package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private float score;
    private String comment;

    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        // Check if there is connectivity
        if(isConnectingToInternet(getApplicationContext()) == false)   {
            builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.checkinternetconnectivity_alertnointernetmessage))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.checkinternetconnectivity_alertnointernetmessage_checkagain), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton( getString(R.string.checkinternetconnectivity_alertnointernetmessage_close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );
            builder.show();
        }

        activity = getIntent().getParcelableExtra("activity");
        activityId = activity.getId();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ratingBar = findViewById(R.id.rev_rating);
        textInputEditText = findViewById(R.id.editTextreview);
        createReview = findViewById(R.id.btn_create_rev);

        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = textInputEditText.getText().toString();
                score = ratingBar.getRating();

                if (comment.isEmpty()) {
                    textInputEditText.setError(getString(R.string.createreviewactivity_insertcomment));
                }
                else {
                    scoreActivityUserWrite(score, comment, activityId, auth.getCurrentUser().getUid(), db);

                    ratingBar.setRating(0F);
                    textInputEditText.getText().clear();

                    Intent intent = new Intent(CreateReviewActivity.this, VisualizeReviewActivity.class);
                    intent.putExtra("activity",activity);
                    startActivity(intent);
                }
            }
        });
    }

    public static void scoreActivityUserWrite(float score, String comment,
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

    // Function that checks if there is internet connection
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            String s = "true";
            Log.i("true wifi",s);
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            String s = "true";
            Log.i("true mobileNetwork",s);
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            String s = "true activeNetwork";
            Log.i("true",s);
            return true;
        }
        String s = "false";
        Log.i("false",s);
        return false;
    }
}