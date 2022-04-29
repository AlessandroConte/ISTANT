package com.example.istant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class activity_createNewLoan extends AppCompatActivity {


    EditText name;
    EditText descr;
    EditText sdate;
    EditText fdate;
    Button btn;

    Calendar dateStart = Calendar.getInstance();

    Calendar dateEnd = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_loan);

        name = (EditText)findViewById(R.id.creteNewLoan_edittext_name);
        descr = (EditText)findViewById(R.id.creteNewLoan_edittext_description);
        sdate = (EditText)findViewById(R.id.createNewLoan_edittext_datestart);
        fdate = (EditText)findViewById(R.id.createNewLoan_edittext_dateend);
        btn = (Button)findViewById(R.id.createNewLoan_button_create);

        DatePickerDialog.OnDateSetListener dateS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateStart.set(Calendar.DAY_OF_MONTH,day);
                dateStart.set(Calendar.MONTH,month);
                dateStart.set(Calendar.YEAR, year);
                updateLabelStart();
            }
        };

        DatePickerDialog.OnDateSetListener dateE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateEnd.set(Calendar.DAY_OF_MONTH,day);
                dateEnd.set(Calendar.MONTH,month);
                dateEnd.set(Calendar.YEAR, year);
                updateLabelEnd();
            }
        };

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_createNewLoan.this,dateS,dateStart.get(Calendar.YEAR),dateStart.get(Calendar.MONTH),dateStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_createNewLoan.this,dateE,dateEnd.get(Calendar.YEAR),dateEnd.get(Calendar.MONTH),dateEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {

                        String textName = name.getText().toString();
                        String textDescr = descr.getText().toString();



                        try{
                            Log.d("Ciao", textDescr);


                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            dateStart.setTime(dateFormat.parse(sdate.getText().toString()));

                            dateEnd.setTime(dateFormat.parse(fdate.getText().toString()));

                            loanWrite(dateStart, dateEnd, textDescr, textName, "",
                                    FirebaseAuth.getInstance().getUid(), FirebaseFirestore.getInstance());

                            Toast.makeText(getApplicationContext(),"Aggiunto con successo",Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){}


                    }
                });

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create New Loan"); // actionbar's name
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


    public static void loanWrite(Calendar dateStart, Calendar dateEnd,
                                 String description, String nameLoan,
                                 String photoLoanObj, String uid, FirebaseFirestore db) {
        Map<String, Object> loan = new HashMap<>();
        Date startLoan = dateStart.getTime();
        Date endLoan = dateEnd.getTime();


        loan.put("description", description);
        loan.put("nameLoan", nameLoan);
        loan.put("photoLoan", photoLoanObj);
        loan.put("uid", uid);
        loan.put("dateStart", new Timestamp(startLoan));
        loan.put("dateEnd", new Timestamp(endLoan));
        String id = SupportFunctions.generateRandomString();

        db.collection("loan").document(id)
                .set(loan)
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

    private void updateLabelStart(){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.ITALY);
        sdate.setText(dateFormat.format(dateStart.getTime()));
    }

    private void updateLabelEnd(){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.ITALY);
        fdate.setText(dateFormat.format(dateEnd.getTime()));
    }
}