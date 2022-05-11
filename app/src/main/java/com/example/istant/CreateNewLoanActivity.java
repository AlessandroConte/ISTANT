package com.example.istant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.istant.model.SupportFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateNewLoanActivity extends AppCompatActivity {

    private ImageView loanImage;
    private EditText name;
    private EditText description;
    private EditText sdate;
    private EditText fdate;
    private Button btn;

    private Calendar dateStart = Calendar.getInstance();
    private Calendar dateEnd = Calendar.getInstance();
    private Uri loanUri;
    private String loanUrl;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_loan_activity);

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
        loanUrl = "";

        loanImage = findViewById(R.id.createNewLoan_imageview_image);
        name = findViewById(R.id.creteNewLoan_edittext_name);
        description = findViewById(R.id.creteNewLoan_edittext_description);
        sdate = findViewById(R.id.createNewLoan_edittext_datestart);
        fdate = findViewById(R.id.createNewLoan_edittext_dateend);
        btn = findViewById(R.id.createNewLoan_button_create);

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

        loanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateNewLoanActivity.this,dateS,dateStart.get(Calendar.YEAR),dateStart.get(Calendar.MONTH),dateStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateNewLoanActivity.this,dateE,dateEnd.get(Calendar.YEAR),dateEnd.get(Calendar.MONTH),dateEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String textName = name.getText().toString();
                String textDescr = description.getText().toString();

                if (textName.isEmpty()) {
                    name.setError(getString(R.string.activitycreatenewloan_givename));
                }
                else {
                    if (textDescr.isEmpty()) {
                        description.setError(getString(R.string.activitycreatenewloan_givedescription));
                    }
                    else {
                        if (sdate.getText().toString().isEmpty()) {
                            sdate.setError(getString(R.string.activitycreatenewloan_givedatestart));
                        }
                        else {
                            if (fdate.getText().toString().isEmpty()) {
                                fdate.setError(getString(R.string.activitycreatenewloan_giveenddate));
                            }
                            else {
                                try{
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    dateStart.setTime(dateFormat.parse(sdate.getText().toString()));
                                    dateEnd.setTime(dateFormat.parse(fdate.getText().toString()));

                                    if (dateStart.compareTo(dateEnd) <= 0) {
                                        loanWrite(dateStart, dateEnd, textDescr, textName, loanUrl, 0, "", FirebaseAuth.getInstance().getUid(), FirebaseFirestore.getInstance());
                                        Toast.makeText(CreateNewLoanActivity.this,getString(R.string.activitycreatenewloan_addedloansuccessfully),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CreateNewLoanActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        fdate.setError(getString(R.string.activitycreatenewloan_errordate));
                                    }
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

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.activitycreatenewloan_createnewloan)); // actionbar's name
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityCreateNewLoan.launch(intent);
    }

    ActivityResultLauncher<Intent> activityCreateNewLoan = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        loanUri = data.getData();
                        loanImage.setImageURI(loanUri);
                        uploadPicture();
                    }
                }
            });

    private void uploadPicture() {
        StorageReference ref = storageReference.child("images/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + auth.getCurrentUser().getUid());
        ref.putFile(loanUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loanUrl = uri.toString();
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

    public static void loanWrite(Calendar dateStart, Calendar dateEnd,
                                 String description, String nameLoan,
                                 String photoLoanObj, int isTaken, String takenUser, String uid, FirebaseFirestore db) {
        Map<String, Object> loan = new HashMap<>();
        Date startLoan = dateStart.getTime();
        Date endLoan = dateEnd.getTime();

        loan.put("description", description);
        loan.put("nameLoan", nameLoan);
        loan.put("photoLoan", photoLoanObj);
        loan.put("isTaken", isTaken);
        loan.put("takenUser", takenUser);
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