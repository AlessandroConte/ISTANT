package com.example.istant;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class activity_createNewActivities extends AppCompatActivity {

    private AlertDialog.Builder builder;

    private ImageView actImage;
    private EditText name;
    private EditText description;
    private EditText sdate;
    private EditText fdate;
    private EditText address;
    private EditText person;

    private Button btn;

    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();
    private Uri actUri;
    private String actUrl;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_activities);

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

        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        actUrl = "";

        actImage = findViewById(R.id.createNewActivity_imageview_image);
        name = findViewById(R.id.creteNewActivity_edittext_name);
        description = findViewById(R.id.creteNewActivity_edittext_description);
        sdate = findViewById(R.id.createNewActivity_edittext_datestart);
        fdate = findViewById(R.id.createNewActivity_edittext_dateend);
        address = findViewById(R.id.createNewActivity_edittext_address);
        btn = findViewById(R.id.createNewActivity_button_create);

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

        actImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_createNewActivities.this,dateS,dateStart.get(Calendar.YEAR),dateStart.get(Calendar.MONTH),dateStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activity_createNewActivities.this,dateE,dateEnd.get(Calendar.YEAR),dateEnd.get(Calendar.MONTH),dateEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String textName = name.getText().toString();
                String textDescr = description.getText().toString();
                String addr = address.getText().toString();
                ArrayList<String> uic = new ArrayList<>();

                try{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    dateStart.setTime(dateFormat.parse(sdate.getText().toString()));
                    dateEnd.setTime(dateFormat.parse(fdate.getText().toString()));

                    uic.add(auth.getCurrentUser().getUid());

                    actWrite(textName, addr, dateStart, dateEnd, textDescr, uic, actUrl, FirebaseFirestore.getInstance());
                    Toast.makeText(getApplicationContext(), getString(R.string.createnewactivity_addactivity),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){}
                actUrl = "";
                actImage.setImageDrawable(null);
                name.getText().clear();
                description.getText().clear();
                sdate.getText().clear();
                fdate.getText().clear();
                address.getText().clear();
            }
        });

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.createnewactivity_actionbar)); // actionbar's name
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityCreateNewActivities.launch(intent);
    }

    ActivityResultLauncher<Intent> activityCreateNewActivities = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        actUri = data.getData();
                        actImage.setImageURI(actUri);
                        uploadPicture();
                    }
                }
            });

    private void uploadPicture() {
        StorageReference ref = storageReference.child("images/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + auth.getCurrentUser().getUid());
        ref.putFile(actUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        actUrl = uri.toString();
                    }
                });
            }
        });
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

    public static void actWrite(String nameActivity, String address,
                                Calendar dateStart, Calendar dateEnd, String description,
                                List<String> personInCharge, String photoEvent, FirebaseFirestore db) {
        Map<String, Object> activity = new HashMap<>();
        Date startEvent = dateStart.getTime();
        Date endEvent = dateEnd.getTime();

        activity.put("address", address);
        activity.put("dateEnd", new Timestamp(endEvent));
        activity.put("dateStart", new Timestamp(startEvent));
        activity.put("description", description);
        activity.put("personInCharge", personInCharge);
        activity.put("nameActivity", nameActivity);
        activity.put("photoEvent", photoEvent);
        String id = SupportFunctions.generateRandomString();

        db.collection("activity").document(id)
                .set(activity)
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
