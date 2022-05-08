package com.example.istant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.istant.model.Activity;
import com.example.istant.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class activity_visualizeactivities extends AppCompatActivity {

    // Firebase variables
    private DocumentReference documentReference;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Retrieving all of the fields of the gui in order to enable and disable the edit options
    private EditText activity_address;
    private EditText activity_dateStart;
    private EditText activity_dateEnd;
    private EditText activity_description;
    private ImageView activity_image;
    private ListView listView_personInCharge;
    private List<String> personInCharge;
    private ArrayAdapter<String> adapter;

    private Button button_review;
    private Button button_participate;
    private Button button_delete;

    // variable needed to retrieve the intent
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_activity);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        activity = getIntent().getParcelableExtra("activity");

        // retrieving the different fields of the gui
        activity_address = findViewById(R.id.visualizeactivities_edittext_address);
        activity_dateStart = findViewById(R.id.visualizeactivities_edittext_dateStart);
        activity_dateEnd = findViewById(R.id.visualizeactivities_edittext_dateEnd);
        activity_description = findViewById(R.id.visualizeactivities_edittext_description);
        activity_image = findViewById(R.id.visualizeactivities_image);
        listView_personInCharge = findViewById(R.id.list_parecipants);

        // adapter = new PersonInChargeAdapter(getApplicationContext(), new ArrayList<String>());
        personInCharge = new ArrayList<>();

        // listView_personInCharge.setAdapter(adapter);

        // retrieving the buttons
        button_review = findViewById(R.id.visualizeactivities_buttonReview);
        button_delete = findViewById(R.id.visualizeactivities_buttonDelete);
        button_participate = findViewById(R.id.visualizeactivities_buttonParticipate);

        String id = activity.getId();
        documentReference = db.collection("activity").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Timestamp dateStart = (Timestamp) documentSnapshot.get("dateStart");
                Timestamp dateEnd = (Timestamp) documentSnapshot.get("dateEnd");

                String time_start = String.valueOf(dateStart.getSeconds());
                String time_end = String.valueOf(dateEnd.getSeconds());
                long timestamp_start = Long.parseLong(time_start)*1000;
                long timestamp_end = Long.parseLong(time_end)*1000;
                Date start = new Date(timestamp_start);
                Date end = new Date(timestamp_end);
                Calendar cstart = Calendar.getInstance();
                Calendar cend = Calendar.getInstance();
                cstart.setTime(start);
                cend.setTime(end);
                int year_start = cstart.get(Calendar.YEAR);
                int year_end = cend.get(Calendar.YEAR);
                int month_start = cstart.get(Calendar.MONTH);
                int month_end = cend.get(Calendar.MONTH);
                int date_start = cstart.get(Calendar.DATE);
                int date_end = cend.get(Calendar.DATE);

                activity_dateStart.setText(String.valueOf(date_start).concat(" - ").concat(String.valueOf(month_start + 1)).concat(" - ").concat(String.valueOf(year_start)));
                activity_dateEnd.setText(String.valueOf(date_end).concat(" - ").concat(String.valueOf(month_end + 1)).concat(" - ").concat(String.valueOf(year_end)));
            }
        });

        activity_address.setText(activity.getAddress());
        activity_description.setText(activity.getDescription());

        button_delete.setBackgroundColor(Color.RED);
        button_participate.setBackgroundColor(Color.GREEN);

        personInCharge = activity.getPersonInCharge();

        // adapter.clear();
        // adapter.addAll(personInCharge);

        if (personInCharge != null && !personInCharge.isEmpty() && personInCharge.contains(auth.getCurrentUser().getUid())) {
            button_delete.setVisibility(View.VISIBLE);
            button_participate.setVisibility(View.INVISIBLE);

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteActivity(db, "activity", id);
                    Toast.makeText(activity_visualizeactivities.this, "Eliminazione avvenuta con successo", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity_visualizeactivities.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
            button_delete.setVisibility(View.INVISIBLE);
            button_participate.setVisibility(View.VISIBLE);

            button_participate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    personInCharge.add(auth.getCurrentUser().getUid());
                    updateDatabaseField(db, "activity", id, "personInCharge", personInCharge);
                    Toast.makeText(activity_visualizeactivities.this, "Ora sei un organizzatore!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity_visualizeactivities.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        if (!activity.getPhotoEvent().equals("")) {
            Glide.with(this).load(activity.getPhotoEvent()).into(activity_image);
        }

        // the button, when pressed, sends the user to the list of the reviews of the activity
        button_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_visualizeactivities.this, VisualizeReviewActivity.class);
                intent.putExtra("activity", activity);
                startActivity(intent);
            }
        });

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Scheda dell'attivtià"); // actionbar's name
        }
    }

    private void deleteActivity(FirebaseFirestore db, String collection, String idDocument) {
        db.collection(collection).document(idDocument).delete();
    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(activity_visualizeactivities.this, MainActivity.class);
        startActivity(intent);
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity_visualizeactivities.this, MainActivity.class);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }

    public static <T> void updateDatabaseField(FirebaseFirestore db, String collectionName,
                                               String idDocument, String nameField, T value) {
        db.collection(collectionName).document(idDocument)
                .update(nameField, value)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Document updated added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error update document", e);
                    }
                });
    }

    /*
    private class PersonInChargeAdapter extends ArrayAdapter<String> {
        List<String> idUsers;

        public PersonInChargeAdapter(@NonNull Context context,List<String> idUsers) {
            super(context, 0, idUsers);
            this.idUsers = idUsers;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_user, parent, false);
            }

            TextView userName = convertView.findViewById(R.id.listadapter_userName);

            String id = idUsers.get(position);

            db.collection("user").whereEqualTo("id", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        userName.setText(document.get("name").toString());
                    }
                }
            });

            Log.d("NAME = ", "" + userName.getText());

            return convertView;
        }
    }

     */
}