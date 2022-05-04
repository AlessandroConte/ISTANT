package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.istant.model.Loan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.Calendar;
import java.util.Date;

public class activity_visualizeloans extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseAuth auth;

    private ImageView image_loan;
    private EditText loan_description;
    private EditText loan_name;
    private EditText loan_startDate;
    private EditText loan_endDate;

    private Button button_modify;
    private Button button_partecipate;
    private Button button_delete;

    private boolean free = false;
    private boolean editable = true;

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
        loan_name = findViewById(R.id.visualizeloans_edittext_name);
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
        loan_name.setText(loan.getNameLoan());

        if (loan.getUid().equals(auth.getCurrentUser().getUid())) {
            button_partecipate.setVisibility(View.INVISIBLE);
            button_modify.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.VISIBLE);
            // TODO: implementare l'onClick dei bottoni

            button_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!editable) {
                        button_modify.setText("Salva");

                        loan_name.setEnabled(true);
                        loan_description.setEnabled(true);
                        loan_startDate.setEnabled(true);
                        loan_endDate.setEnabled(true);

                        editable = true;
                    }
                    else {
                        button_modify.setText("Modifica");

                        // TODO : guarda crea loan
                    }
                }
            });

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDatabaseDocument(db, "loan", loan.getId());
                    Toast.makeText(activity_visualizeloans.this, "Prestito cancellato correttamente", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            button_partecipate.setVisibility(View.VISIBLE);
            button_modify.setVisibility(View.INVISIBLE);
            button_delete.setVisibility(View.INVISIBLE);

            if (loan.getIsTaken() == 0) {
                button_partecipate.setBackgroundColor(Color.GREEN);
                free = true;
            }

            if (loan.getIsTaken() == 1) {
                if (loan.getTakenUser().equals(auth.getCurrentUser().getUid())) {
                    button_partecipate.setText("Restituisci");
                    button_partecipate.setBackgroundColor(Color.YELLOW);
                }
                else {
                    button_partecipate.setText("Occupato");
                    button_partecipate.setBackgroundColor(Color.RED);
                    button_partecipate.setClickable(false);
                }
            }

            button_partecipate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (free) {
                        updateDatabaseField(db, "loan", loan.getId(), "isTaken", 1);
                        updateDatabaseField(db, "loan", loan.getId(), "takenUser", auth.getCurrentUser().getUid());
                        Toast.makeText(activity_visualizeloans.this, "Oggetto preso in prestito correttamente!", Toast.LENGTH_SHORT).show();
                        // TODO: implementare tornare indietro
                    }
                    else {
                        updateDatabaseField(db, "loan", loan.getId(), "isTaken", 0);
                        updateDatabaseField(db, "loan", loan.getId(), "takenUser", "");
                        Toast.makeText(activity_visualizeloans.this, "Hai restituito correttamente il prestito!", Toast.LENGTH_SHORT).show();
                        // TODO: implementare tornare indietro
                    }
                }
            });

        }
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

    public static void deleteDatabaseDocument(FirebaseFirestore db, String collectionName,
                                              String idDocument) {
        db.collection(collectionName).document(idDocument).delete();
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