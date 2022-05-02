package com.example.istant;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.istant.model.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private RatingBar activity_ratingBar;
    private ImageView activity_image;
    private List<String> personInCharge;

    private Button button_visualizeParticipants;
    private Button button_participate;
    private Button button_modifysave;
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
        activity_ratingBar = findViewById(R.id.visualizeactivities_ratingbar_rating);
        activity_image = findViewById(R.id.visualizeactivities_image);

        // retrieving the buttons
        button_visualizeParticipants = findViewById(R.id.visualizeactivities_buttonVisualizeParticipants);
        button_delete = findViewById(R.id.visualizeactivities_buttonDelete);
        // WE NEED TO SEE IF THE USER THAT IS OPENING THIS SCREEN IS THE CREATER OF THIS ACTIVITY
        // IF SO WE NEED TO SET THIS BUTTON VISIBLE
        //button_delete.setVisibility(View.INVISIBLE);  // THIS LINE OF CODE MAKES THE BUTTON INVISIBLE
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

        // personInCharge = activity.getPersonInCharge(); TODO: da implementare personInCharge
        // Log.d("PERSON IN CHARGE", "" + personInCharge.size());

        if (!activity.getPhotoEvent().equals("")) {
            Glide.with(this).load(activity.getPhotoEvent()).into(activity_image);
        }

        // the button, when pressed, sends the user to the list of the participants of the activity
        button_visualizeParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), activity_visualizeactivities_visualizeparticipants.class));
                finish();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DELETE THE ACTIVITY!!!
            }
        });

        button_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD ACTIONS TO PARTICIPATE !!!
            }
        });

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Scheda dell'attivtià"); // actionbar's name
        }
    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}