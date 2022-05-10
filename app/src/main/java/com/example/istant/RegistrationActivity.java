package com.example.istant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private TextView tv_gdpr;
    private EditText regNome, regCognome, regEmail, regAddress, regPhoneNumber, regBornDate, regFiscalCode, regPass, regConfPass;
    private ImageView regPic;
    private RadioButton regSex_m, regSex_f;
    private Button regButton,gotoLogin, gdpr;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private StorageReference storageReference;

    private Uri imageUri;
    private String id;
    private String nome;
    private String cognome;
    private String indirizzo;
    private String numeroTelefono;
    private Timestamp dataNascita;
    private Calendar dateBorn;
    private String CF;
    private int sesso = 0;
    private String email;
    private String pass;
    private String confpass;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

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

        regPic = findViewById(R.id.registrationProfilePicture);

        regNome = findViewById(R.id.registrationName);
        regCognome = findViewById(R.id.registrationSurname);
        regEmail = findViewById(R.id.registrationEmail);
        regAddress = findViewById(R.id.registrationAddress);
        regPhoneNumber = findViewById(R.id.registrationPhoneNumber);
        regBornDate = findViewById(R.id.registrationBornDate);
        regFiscalCode = findViewById(R.id.registrationFiscalCode);
        regPass = findViewById(R.id.registrationPassword);
        regConfPass = findViewById(R.id.registrationConfPassword);

        regSex_m = findViewById(R.id.registration_genderradiobutton_m);
        regSex_f = findViewById(R.id.registration_genderradiobutton_f);
        regButton = findViewById(R.id.registrationButton);
        gotoLogin = findViewById(R.id.gotologinButton);
        tv_gdpr = findViewById(R.id.tv_gdpr);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        dateBorn = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateBorn.set(Calendar.DAY_OF_MONTH, day);
                dateBorn.set(Calendar.MONTH, month);
                dateBorn.set(Calendar.YEAR, year);
                updateLabel();
            }
        };

        regPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        regBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegistrationActivity.this, date, dateBorn.get(Calendar.YEAR), dateBorn.get(Calendar.MONTH), dateBorn.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        regSex_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesso = 0;
            }
        });

        regSex_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesso = 1;
            }
        });

        tv_gdpr.setPaintFlags(tv_gdpr.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        tv_gdpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GdprActivity.class));
                finish();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);

                nome = regNome.getText().toString();
                cognome = regCognome.getText().toString();
                indirizzo = regAddress.getText().toString();
                numeroTelefono = regPhoneNumber.getText().toString();
                CF = regFiscalCode.getText().toString();
                email = regEmail.getText().toString();
                pass = regPass.getText().toString();
                confpass = regConfPass.getText().toString();

                if (nome.isEmpty()){
                    regNome.setError(getString(R.string.registrationactivity_namerequired));
                }
                else {
                    if (cognome.isEmpty()){
                        regCognome.setError(getString(R.string.registrationactivity_surnamerequired));
                    }
                    else {
                        if (regBornDate.getText().toString().isEmpty()) {
                            regBornDate.setError(getString(R.string.registrationactivity_dateofbirthrequired));
                        }
                        else {
                            if(email.isEmpty()){
                                regEmail.setError(getString(R.string.registrationactivity_emailrequired));
                            }
                            else {
                                if(pass.isEmpty()){
                                    regPass.setError(getString(R.string.registrationactivity_passwordrequired));
                                }
                                else {
                                    if(confpass.isEmpty()){
                                        regConfPass.setError(getString(R.string.registrationactivity_passwordrequired));
                                    }
                                    else {
                                        if (!pass.equals(confpass)){
                                            regConfPass.setError(getString(R.string.registrationactivity_samepasswordsrequired));
                                        }
                                        else {
                                            try {
                                                dateBorn.setTime(dateFormat.parse(regBornDate.getText().toString()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            dataNascita = new Timestamp(dateBorn.getTime());

                                            fAuth.createUserWithEmailAndPassword(email, pass)
                                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            // AdditionalUserInfo info = authResult.getAdditionalUserInfo();
                                                            // String id = info.getProviderId();

                                                            id = fAuth.getCurrentUser().getUid();

                                                            userWrite(id, indirizzo, dataNascita, email, CF, sesso, "", nome, cognome, numeroTelefono, db);
                                                            Toast.makeText(RegistrationActivity.this, getString(R.string.registrationactivity_datacorrect), Toast.LENGTH_SHORT).show();
                                                            uploadPicture();

                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ITALY);
        regBornDate.setText(dateFormat.format(dateBorn.getTime()));
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        registrationActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> registrationActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        regPic.setImageURI(imageUri);
                    }
                }
            });

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        StorageReference ref = storageReference.child("images/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid() + "/" + fAuth.getCurrentUser().getUid());

        pd.setTitle(getString(R.string.registrationactivity_imageuploading));
        pd.show();

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateDatabaseField(db,"user", id,"photoURL", uri.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationActivity.this, getString(R.string.registrationactivity_uploaderror), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Snackbar.make(RegistrationActivity.this.findViewById(android.R.id.content), getString(R.string.registrationactivity_imageuploaded),Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(RegistrationActivity.this.getApplicationContext(), getString(R.string.registrationactivity_uploadfailed), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage( getString(R.string.registrationactivity_percentage) + ": " + (int) progressPercent + "%");
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

    public static void userWrite(String id, String address, Timestamp dateBorn, String email,
                                 String fiscalCode, int gender, String photoUrl, String name,
                                 String surname, String telephoneNumber, FirebaseFirestore db) {
        Map<String, Object> user = new HashMap<>();

        user.put("address", address);
        user.put("dateBorn", dateBorn);
        user.put("email", email);
        user.put("fiscalCode", fiscalCode);
        user.put("gender", gender);
        user.put("photoURL", photoUrl);
        user.put("name", name);
        user.put("surname", surname);
        user.put("telephoneNumber", telephoneNumber);


        db.collection("user").document(id)
                .set(user)
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