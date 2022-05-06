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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.istant.model.Child;
import com.example.istant.model.ScoreActivityUser;
import com.example.istant.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class VisualizeReviewActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<User> userArrayList;
    Child child;

    private ListView reviewlistview;
    private FirebaseFirestore db;
    private ArrayAdapter<ScoreActivityUser> adapter;
    private Context context;
    private ArrayList<ScoreActivityUser> review;


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


        db = FirebaseFirestore.getInstance();
        reviewlistview = findViewById(R.id.review_list);

        adapter = new VisualizeReviewActivity.ReviewAdapter(this, new ArrayList<ScoreActivityUser>());
        reviewlistview.setAdapter(adapter);
        reviewlistview.setClickable(true);
        review = new ArrayList<ScoreActivityUser>();


        db.collection("scoreActivityUser")
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

                                //scores.add(scoreActUser);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        Button newReview = findViewById(R.id.create_review_btn);
        newReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizeReviewActivity.this, CreateReviewActivity.class);
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

            TextView revName = convertView.findViewById(R.id.listadapter_revName);
            TextView revScore = convertView.findViewById(R.id.listadapter_revScore);
            TextView revText = convertView.findViewById(R.id.listadapter_revText);

            ScoreActivityUser rev = review.get(position);

            revName.setText(rev.getId());
            revScore.setText(rev.getScore());
            revText.setText(rev.getComment());

            return convertView;
        }
    }
}