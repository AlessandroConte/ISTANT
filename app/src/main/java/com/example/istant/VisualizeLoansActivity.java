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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.bumptech.glide.Glide;
import com.example.istant.model.Loan;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class VisualizeLoansActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    private FirebaseFirestore db;
    private StorageReference storageReference;
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
    private boolean editable;

    // variable needed to retrieve the intent
    private Loan loan;

    private Uri imageUri;
    private String name;
    private String description;
    private Calendar dateStart;
    private Calendar dateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizeloans_activity);

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.activityvisualizeloans_titoloactivity)); // actionbar's name
        }

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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        loan = getIntent().getParcelableExtra("loan");
        dateStart = Calendar.getInstance();
        dateEnd = Calendar.getInstance();

        image_loan = findViewById(R.id.visualizeloans_image);
        loan_description = findViewById(R.id.visualizeloans_edittext_description);
        loan_name = findViewById(R.id.visualizeloans_edittext_name);
        loan_startDate = findViewById(R.id.visualizeloans_edittext_datestart);
        loan_endDate = findViewById(R.id.visualizeloans_edittext_dateend);

        button_modify = findViewById(R.id.visualizeloans_buttonModifySave);
        button_delete = findViewById(R.id.visualizeloans_buttonDelete);
        button_partecipate = findViewById(R.id.visualizeloans_buttonParticipate);

        image_loan.setClickable(false);

        DatePickerDialog.OnDateSetListener dateS = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateStart.set(Calendar.DAY_OF_MONTH, day);
                dateStart.set(Calendar.MONTH, month);
                dateStart.set(Calendar.YEAR, year);
                updateLabelStart();
            }
        };

        DatePickerDialog.OnDateSetListener dateE = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateEnd.set(Calendar.DAY_OF_MONTH, day);
                dateEnd.set(Calendar.MONTH, month);
                dateEnd.set(Calendar.YEAR, year);
                updateLabelEnd();
            }
        };

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
                /*
                int year_start = cstart.get(Calendar.YEAR);
                int year_end = cend.get(Calendar.YEAR);
                int month_start = cstart.get(Calendar.MONTH);
                int month_end = cend.get(Calendar.MONTH);
                int date_start = cstart.get(Calendar.DATE);
                int date_end = cend.get(Calendar.DATE);

                 */
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);

                // loan_startDate.setText(String.valueOf(date_start).concat(" - ").concat(String.valueOf(month_start + 1)).concat(" - ").concat(String.valueOf(year_start)));
                // loan_endDate.setText(String.valueOf(date_end).concat(" - ").concat(String.valueOf(month_end + 1)).concat(" - ").concat(String.valueOf(year_end)));

                loan_startDate.setText(dateFormat.format(start));
                loan_endDate.setText(dateFormat.format(end));
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

            button_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!editable) {
                        button_modify.setText(getString(R.string.activityvisualizeloans_save));

                        image_loan.setClickable(true);
                        loan_name.setEnabled(true);
                        loan_description.setEnabled(true);
                        loan_startDate.setEnabled(true);
                        loan_endDate.setEnabled(true);

                        image_loan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                choosePicture();
                            }
                        });

                        loan_startDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new DatePickerDialog(VisualizeLoansActivity.this, dateS, dateStart.get(Calendar.YEAR), dateStart.get(Calendar.MONTH), dateStart.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        loan_endDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new DatePickerDialog(VisualizeLoansActivity.this, dateE, dateEnd.get(Calendar.YEAR), dateEnd.get(Calendar.MONTH), dateEnd.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        editable = true;
                    }
                    else {
                        button_modify.setText(getString(R.string.activityvisualizeloans_modifiy));

                        name = loan_name.getText().toString();
                        description = loan_description.getText().toString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
                        try {
                            dateStart.setTime(dateFormat.parse(loan_startDate.getText().toString()));
                            dateEnd.setTime(dateFormat.parse(loan_endDate.getText().toString()));

                            Map<String, Object> loan = new HashMap<>();
                            loan.put("nameLoan", name);
                            loan.put("description", description);
                            loan.put("dateStart", new Timestamp(dateStart.getTime()));
                            loan.put("dateEnd", new Timestamp(dateEnd.getTime()));

                            db.collection("loan").document(id)
                                    .update(loan)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_successfullupdate), Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_failedupdate), Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        image_loan.setClickable(false);
                        loan_name.setEnabled(false);
                        loan_description.setEnabled(false);
                        loan_startDate.setEnabled(false);
                        loan_endDate.setEnabled(false);

                        editable = false;
                    }
                }
            });

            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDatabaseDocument(db, "loan", loan.getId());
                    Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_successfullloandeletion), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(VisualizeLoansActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
            button_partecipate.setVisibility(View.VISIBLE);
            button_modify.setVisibility(View.INVISIBLE);
            button_delete.setVisibility(View.INVISIBLE);

            if (loan.getIsTaken() == 0) {
                button_partecipate.setBackgroundColor(Color.parseColor("#006400"));
                free = true;
            }

            if (loan.getIsTaken() == 1) {
                if (loan.getTakenUser().equals(auth.getCurrentUser().getUid())) {
                    button_partecipate.setText(getString(R.string.activityvisualizeloans_giveback));
                    button_partecipate.setBackgroundColor(Color.parseColor("#F6BE00"));
                }
                else {
                    button_partecipate.setText(getString(R.string.activityvisualizeloans_notavailable));
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
                        Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_loanstartedcorrectly), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(VisualizeLoansActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        updateDatabaseField(db, "loan", loan.getId(), "isTaken", 0);
                        updateDatabaseField(db, "loan", loan.getId(), "takenUser", "");
                        Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_gavebackloan), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(VisualizeLoansActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

    private void updateLabelEnd() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ITALY);
        loan_endDate.setText(dateFormat.format(dateEnd.getTime()));
    }

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ITALY);
        loan_startDate.setText(dateFormat.format(dateStart.getTime()));
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        visualizeLoansResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> visualizeLoansResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        image_loan.setImageURI(imageUri);
                        uploadPicture();
                    }
                }
            });

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        StorageReference ref = storageReference.child("loans/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + auth.getCurrentUser().getUid());

        pd.setTitle(getString(R.string.activityvisualizeloans_caricamentoimmagine));
        pd.show();

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateDatabaseField(db,"loan", loan.getId(),"photoLoan", uri.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VisualizeLoansActivity.this, getString(R.string.activityvisualizeloans_loadingerror), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Snackbar.make(VisualizeLoansActivity.this.findViewById(android.R.id.content), getString(R.string.activityvisualizeloans_imageloaded),Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(VisualizeLoansActivity.this.getApplicationContext(), getString(R.string.activityvisualizeloans_unsuccessfullupload), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage(getString(R.string.registrationactivity_percentage)+": " + (int) progressPercent + "%");
                    }
                });
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