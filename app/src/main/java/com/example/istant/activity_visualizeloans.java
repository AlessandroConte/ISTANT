package com.example.istant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.istant.model.Loan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;

public class activity_visualizeloans extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseAuth auth;

    private ImageView image_loan;
    private EditText loan_description;
    private EditText loan_startDate;
    private EditText loan_endDate;

    private Button button_modify;
    private Button button_partecipate;
    private Button button_delete;

    // variable needed to retrieve the intent
    private Loan loan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizeloans);

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Scheda del prestito"); // actionbar's name
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loan = getIntent().getParcelableExtra("loan");

        image_loan = findViewById(R.id.visualizeloans_image);
        loan_description = findViewById(R.id.visualizeloans_edittext_description);
        loan_startDate = findViewById(R.id.visualizeloans_edittext_datestart);
        loan_endDate = findViewById(R.id.visualizeloans_edittext_dateend);

        button_modify = findViewById(R.id.visualizeloans_buttonModifySave);
        button_delete = findViewById(R.id.visualizeloans_buttonDelete);
        button_partecipate = findViewById(R.id.visualizeloans_buttonParticipate);

        // Getting the information from the clicked item
        String id = loan.getId();
        documentReference = db.collection("loan").document(id);
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

                loan_startDate.setText(String.valueOf(date_start).concat(" - ").concat(String.valueOf(month_start + 1)).concat(" - ").concat(String.valueOf(year_start)));
                loan_endDate.setText(String.valueOf(date_end).concat(" - ").concat(String.valueOf(month_end + 1)).concat(" - ").concat(String.valueOf(year_end)));
            }
        });
        if (!loan.getPhotoLoan().equals("")){
            Glide.with(this).load(loan.getPhotoLoan()).into(image_loan);
        }
        loan_description.setText(loan.getDescription());

        if (loan.getUid().equals(auth.getCurrentUser().getUid())) {
            button_partecipate.setVisibility(View.INVISIBLE);
            button_modify.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);
            // TODO: implementare l'onClick dei bottoni
        }
        else {
            button_partecipate.setVisibility(View.VISIBLE);
            button_modify.setVisibility(View.INVISIBLE);
            button_delete.setVisibility(View.INVISIBLE);
            // TODO: implementare l'onClick dei bottoni
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