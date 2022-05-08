package com.example.istant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.example.istant.model.Activity;
import com.example.istant.model.ScoreActivityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class VisualizeReviewActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    private ListView reviewlistview;
    private TextView score;
    private FirebaseFirestore db;
    private ArrayAdapter<ScoreActivityUser> adapter;
    private ArrayList<ScoreActivityUser> review;
    private FirebaseAuth auth;
    private SwipeRefreshLayout refreshLayout;

    private String idActivity;
    private Activity activity;
    private Button newReview;
    private Button deleteReview;
    private String idDelete;

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

        // Check if there is connectivity
        if(isConnectingToInternet(getApplicationContext()) == false)   {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Internet Connection NOT available")
                    .setCancelable(true)
                    .setPositiveButton("Check Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );
            builder.show();
        }

        activity = getIntent().getParcelableExtra("activity");
        idActivity = activity.getId();

        db = FirebaseFirestore.getInstance();
        reviewlistview = findViewById(R.id.review_list);
        newReview = findViewById(R.id.create_review_btn);
        deleteReview = findViewById(R.id.delete_review_btn);
        score = findViewById(R.id.vis_rev_tv_title);
        refreshLayout = findViewById(R.id.swipeRefresh_visualizeReviews);

        auth = FirebaseAuth.getInstance();
        adapter = new VisualizeReviewActivity.ReviewAdapter(this, new ArrayList<ScoreActivityUser>());
        reviewlistview.setAdapter(adapter);
        review = new ArrayList<ScoreActivityUser>();

        newReview.setVisibility(View.VISIBLE);
        deleteReview.setVisibility(View.INVISIBLE);

        visualizeScore();
        visualizeReviews();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                review.clear();
                visualizeReviews();
                refreshLayout.setRefreshing(false);
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

        deleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment(db, "scoreActivityUser", idDelete);
                Toast.makeText(getApplicationContext(), "Cancellazione avvenuta con successo!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(VisualizeReviewActivity.this, activity_visualizeactivities.class);
                intent.putExtra("activity", activity);
                startActivity(intent);
            }
        });
    }

    public static void deleteComment(FirebaseFirestore db, String collectionName, String idDocument) {
        db.collection(collectionName).document(idDocument).delete();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), activity_visualizeactivities.class);
        myIntent.putExtra("activity", activity);
        startActivity(myIntent);
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisualizeReviewActivity.this, activity_visualizeactivities.class);
        intent.putExtra("activity", activity);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }

    public void visualizeScore () {
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
                                float score = Float.parseFloat(document.getData().get("score").toString());
                                total_stars += score;
                            }

                            if (number_score == 0) {
                                score.setText("Nessuna recensione");
                            }
                            else {
                                result = (float) total_stars / number_score;
                                String res = String.format("%.1f", result);
                                score.setText("Punteggio medio = " + res);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), "Errore lato server, ci scusiamo per il disagio!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void visualizeReviews () {
        db.collection("scoreActivityUser")
                .whereEqualTo("activityid", idActivity)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                float score = Float.parseFloat(document.getData().get("score").toString());
                                String comment = document.getData().get("comment").toString();
                                String idActivity = document.getData().get("activityid").toString();
                                String uid = document.getData().get("uid").toString();

                                ScoreActivityUser scoreActUser = new ScoreActivityUser(id, score, comment, idActivity, uid);

                                if (uid.equals(auth.getCurrentUser().getUid())) {
                                    newReview.setVisibility(View.INVISIBLE);
                                    deleteReview.setVisibility(View.VISIBLE);
                                    idDelete = id;
                                }
                                review.add(scoreActUser);
                            }
                            adapter.clear();
                            adapter.addAll(review);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), "Errore lato server, ci scusiamo per il disagio!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

            revName.setText(rev.getUid());
            revScore.setText(Float.toString(rev.getScore()));
            revText.setText(rev.getComment());

            return convertView;
        }
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