package com.example.istant;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.appcompat.app.AlertDialog;
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

    private AlertDialog.Builder builder;

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

        // Check if there is connectivity
        if(isConnectingToInternet(getApplicationContext()) == false)   {
            builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.checkinternetconnectivity_alertnointernetmessage))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.checkinternetconnectivity_alertnointernetmessage_checkagain), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton( getString(R.string.checkinternetconnectivity_alertnointernetmessage_close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );
            builder.show();
        }

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
                            tv_name.setError("");
                        }
                        else {
                            if (textSurname.isEmpty()) {
                                tv_surname.setError(getString(R.string.activitycreatenewchild_givename));
                            }
                            else {
                                if (rb_sex_m.getText().toString().isEmpty() && rb_sex_f.getText().toString().isEmpty()) {
                                    rb_sex_m.setError(getString(R.string.activitycreatenewchild_givesex));
                                }
                                else {
                                    if (dateB.getText().toString().isEmpty()) {
                                        dateB.setError(getString(R.string.activitycreatenewchild_givedateofbirth));
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

                                            Toast.makeText(activity_createNewChild.this,getString(R.string.activitycreatenewchild_addedchild),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(activity_createNewChild.this, activity_settings_managechildren.class);
                                            startActivity(intent);

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
