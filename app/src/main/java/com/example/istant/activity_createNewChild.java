package com.example.istant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class activity_createNewChild extends AppCompatActivity {


    Calendar dateBorn = Calendar.getInstance();


    // Retrieveing all of the fields of the gui in order to enable and disable the edit options
    private TextView tv_name;
    private TextView tv_surname;
    private EditText dateB;
    private RadioButton rb_sex_m;
    private RadioButton rb_sex_f;

    // button used to modify / save the user information
    private Button btnCreate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_child);


        // retrieving the different fields of the gui
        tv_name = (EditText)findViewById(R.id.fragmentChild_edittext_name);
        tv_surname = (EditText)findViewById(R.id.fragmentChild_edittext_surname);
        rb_sex_m = (RadioButton) findViewById(R.id.fragmentChild_genderradiobutton_m);
        rb_sex_f = (RadioButton) findViewById(R.id.fragmentChild_genderradiobutton_f);
        dateB = (EditText)findViewById(R.id.fragmentChild_edittext_dateofbirth);



        // Retrieving the two buttons
        btnCreate = (Button)findViewById(R.id.fragmentChild_buttonCreate);



        DatePickerDialog.OnDateSetListener dateBo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateBorn.set(Calendar.DAY_OF_MONTH,day);
                dateBorn.set(Calendar.MONTH,month);
                dateBorn.set(Calendar.YEAR, year);
                updateLabelStart();
            }
        };

        dateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_createNewChild.this,dateBo,dateBorn.get(Calendar.YEAR),dateBorn.get(Calendar.MONTH),dateBorn.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Now we handle the buttons and the ImageView

        btnCreate.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {

                        String textName = tv_name.getText().toString();
                        String textSurname = tv_surname.getText().toString();
                        Log.d("ok", rb_sex_m.getText().toString());
                        int textGender;
                        if((rb_sex_m.getText().toString() == "M")) {
                            textGender = 1;
                        }
                        else{
                            textGender = 0;
                        }


                        try{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            dateBorn.setTime(dateFormat.parse(dateB.getText().toString()));

                            childWrite(null, dateBorn, textGender, "", textName, textSurname,
                                    FirebaseAuth.getInstance().getUid(), FirebaseFirestore.getInstance());

                            Toast.makeText(getApplicationContext(),"Aggiunto con successo",Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){}


                    }
                });


    }


    // CHILD - write
    public static void childWrite(List<String> allergy, Calendar dateBorn, int gender,
                                  String info, String name, String surname, String parent, FirebaseFirestore db) {

        Map<String, Object> child = new HashMap<>();
        Date born = dateBorn.getTime();

        child.put("allergy", allergy);
        child.put("gender", gender);
        child.put("info", info);
        child.put("name", name);
        child.put("surname", surname);
        child.put("uid", parent);
        child.put("bornDate", new Timestamp(born));
        String id = SupportFunctions.generateRandomString();

        db.collection("child").document(id)
                .set(child)
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

    @Override
    public void onResume() {
        super.onResume();

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

    private void updateLabelStart(){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.ITALY);
        dateB.setText(dateFormat.format(dateBorn.getTime()));
    }


}
