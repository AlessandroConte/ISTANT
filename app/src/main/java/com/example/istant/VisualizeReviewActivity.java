package com.example.istant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.istant.model.Activity;
import com.example.istant.model.ScoreActivityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class VisualizeReviewActivity extends AppCompatActivity {

    private ListView reviewlistview;
    private TextView score;
    private FirebaseFirestore db;
    private ArrayAdapter<ScoreActivityUser> adapter;
    private ArrayList<ScoreActivityUser> review;

    private String idActivity;
    private Activity activity;
    private Button newReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_review);

        // This allows to the actionbar to have a back button pointing to the last activity you visited
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Recensioni");
        }

        activity = getIntent().getParcelableExtra("activity");
        idActivity = activity.getId();

        db = FirebaseFirestore.getInstance();
        reviewlistview = findViewById(R.id.review_list);
        newReview = findViewById(R.id.create_review_btn);
        score = findViewById(R.id.vis_rev_tv_title);

        adapter = new VisualizeReviewActivity.ReviewAdapter(this, new ArrayList<ScoreActivityUser>());
        reviewlistview.setAdapter(adapter);
        review = new ArrayList<ScoreActivityUser>();

        db.collection("scoreActivityUser")
                .whereEqualTo("activityid", idActivity)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total_stars = 0;
                            int number_score = 0;
                            ArrayList<String> comments = new ArrayList<>();
                            float result;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                number_score += 1;
                                comments.add(document.getData().get("comment").toString());
                                int score = Integer.parseInt(document.getData().get("score").toString());
                                total_stars += score;
                            }

                            result = (float) total_stars / number_score;
                            String res = String.format("%.1f", result);
                            score.setText("Punteggio medio = " + res);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("scoreActivityUser")
                .whereEqualTo("activityid", idActivity)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                int score = Integer.parseInt(document.getData().get("score").toString());
                                String comment = document.getData().get("comment").toString();
                                String idActivity = document.getData().get("activityid").toString();
                                String uid = document.getData().get("uid").toString();

                                ScoreActivityUser scoreActUser = new ScoreActivityUser(id, score, comment, idActivity, uid);

                                review.add(scoreActUser);
                            }
                            adapter.clear();
                            adapter.addAll(review);

                            // controllo

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        newReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizeReviewActivity.this, CreateReviewActivity.class);
                intent.putExtra("activity",activity);
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), activity_visualizeactivities.class);
        startActivity(myIntent);
        return true;
    }

    private class ReviewAdapter extends ArrayAdapter<ScoreActivityUser> {
        ArrayList<ScoreActivityUser> review;

        public ReviewAdapter(@NonNull Context context, ArrayList<ScoreActivityUser> review) {
            super(context, 0, review);
            this.review = review;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_reviews,parent, false);
            }

            // TextView revName = convertView.findViewById(R.id.listadapter_revName);
            // TextView revScore = convertView.findViewById(R.id.listadapter_revScore);
            TextView revText = convertView.findViewById(R.id.listadapter_revText);

            ScoreActivityUser rev = review.get(position);

            // revName.setText(rev.getUid());
            // revScore.setText(rev.getScore());
            revText.setText(rev.getComment());

            return convertView;
        }
    }
}