package com.example.istant;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class activity_createNewChild extends AppCompatActivity {

    private Calendar dateBorn = Calendar.getInstance();

    // Retrieveing all of the fields of the gui in order to enable and disable the edit options
    private EditText tv_name;
    private EditText tv_surname;
    private TextView tv_gender;
    private EditText dateB;
    private RadioButton rb_sex_m;
    private RadioButton rb_sex_f;

    // button used to modify / save the user information
    private Button btnCreate;

    private int gender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_child);

        // retrieving the different fields of the gui
        tv_name = (EditText) findViewById(R.id.fragmentChild_edittext_name);
        tv_surname = (EditText)findViewById(R.id.fragmentChild_edittext_surname);
        tv_gender = (TextView) findViewById(R.id.fragmentChild_gender);
        rb_sex_m = (RadioButton) findViewById(R.id.fragmentChild_genderradiobutton_m);
        rb_sex_f = (RadioButton) findViewById(R.id.fragmentChild_genderradiobutton_f);
        dateB = (EditText) findViewById(R.id.fragmentChild_edittext_dateofbirth);

        // Retrieving the two buttons
        btnCreate = (Button) findViewById(R.id.fragmentChild_buttonCreate);

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

        rb_sex_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = 0;
            }
        });

        rb_sex_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = 1;
            }
        });

        // Now we handle the buttons and the ImageView

        btnCreate.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        String textName = tv_name.getText().toString();
                        String textSurname = tv_surname.getText().toString();

                        if (textName.isEmpty()) {
                            tv_name.setError("Il nome deve essere fornito!");
                        }
                        else {
                            if (textSurname.isEmpty()) {
                                tv_surname.setError("Il cognome deve essere fornito!");
                            }
                            else {
                                if (rb_sex_m.getText().toString().isEmpty() && rb_sex_f.getText().toString().isEmpty()) {
                                    rb_sex_m.setError("Il sesso deve essere fornito!");
                                }
                                else {
                                    if (dateB.getText().toString().isEmpty()) {
                                        dateB.setError("La data di nascita deve essere fornita!");
                                    }
                                    else {
                                        try{
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
                                            dateBorn.setTime(dateFormat.parse(dateB.getText().toString()));

                                            childWrite(null, dateBorn, gender, "", textName, textSurname, FirebaseAuth.getInstance().getUid(), FirebaseFirestore.getInstance());

                                            tv_name.getText().clear();
                                            tv_surname.getText().clear();
                                            rb_sex_m.setChecked(false);
                                            rb_sex_f.setChecked(false);
                                            dateB.getText().clear();

                                            Toast.makeText(activity_createNewChild.this,"Figlio aggiunto con successo!",Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
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
