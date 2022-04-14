package com.example.istant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class activity_settings extends AppCompatActivity {
    private Button btnSaveChanges, btnManageChildren;

    // Variables needed to change the picture of the activity
    private ImageView activityImage;
    private Uri imageUri;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // variable needed to make the fields editable.
    private boolean flag;

    // Retrieveing all of the fileds of the gui in order to enable and disable the edit options
    private TextView tv_name;
    private TextView tv_surname;
    private TextView tv_phonenumber;
    private TextView tv_fiscalcode;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_dateofbirth;
    private RadioButton rb_sex_m;
    private RadioButton rb_sex_f;

    // button used to modify / save the user information
    private Button button_modifysave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }

        // retrieving the different fields of the gui
        tv_name = findViewById(R.id.settings_edittext_name);
        tv_surname = findViewById(R.id.settings_edittext_surname);
        tv_phonenumber = findViewById(R.id.settings_edittext_phonenumber);
        tv_fiscalcode = findViewById(R.id.settings_edittext_fiscalcode);
        tv_address = findViewById(R.id.settings_edittext_address);
        tv_email = findViewById(R.id.settings_edittext_email);
        tv_dateofbirth = findViewById(R.id.settings_edittext_dateofbirth);
        rb_sex_m = findViewById(R.id.settings_genderradiobutton_m);
        rb_sex_f = findViewById(R.id.settings_genderradiobutton_f);


        // retrieve the button "modify/save" from the gui
        // if the bool "flag" is:
        // false -> the textfields aren't editable and only show the data present at the given moment in the db. The button_modifisave has as text "modify"
        // true -> the textfields are editable and the user can change the fileds. The button has as text "save"
        button_modifysave = findViewById(R.id.settings_buttonModifySave);
        button_modifysave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // false - it enables the fileds and the user can modify them, at the end it puts the flag as true
                if ( flag == false ) {
                    button_modifysave.setText("Save");
                    tv_name.setEnabled(true);
                    tv_surname.setEnabled(true);
                    tv_phonenumber.setEnabled(true);
                    tv_fiscalcode.setEnabled(true);
                    tv_address.setEnabled(true);
                    tv_address.setEnabled(true);
                    tv_email.setEnabled(true);
                    tv_dateofbirth.setEnabled(true);
                    rb_sex_m.setEnabled(true);
                    rb_sex_f.setEnabled(true);
                    flag = true;
                }
                // true - the user modified the field/s and now wants to save. It saves the modified values, it disables the fileds and then sets the falg to false.
                else {
                    // save the values contained in the fileds in the activity info
                    button_modifysave.setText("Modify");
                    tv_name.setEnabled(false);
                    tv_surname.setEnabled(false);
                    tv_phonenumber.setEnabled(false);
                    tv_fiscalcode.setEnabled(false);
                    tv_address.setEnabled(false);
                    tv_address.setEnabled(false);
                    tv_email.setEnabled(false);
                    tv_dateofbirth.setEnabled(false);
                    rb_sex_m.setEnabled(false);
                    rb_sex_f.setEnabled(false);
                    flag = false;
                }

            }
        });


        // retrieve the two buttons for the graphical user interface
        btnSaveChanges = findViewById(R.id.settings_buttonSave);
        btnManageChildren = findViewById(R.id.settings_buttonManageChildren);

        // set actions to perform when the btnSaveChanges is clicked
        // TO DO

        // Here I am retrievenig the ImageView and setting an action when this is executed
        // The actions allows the user to change/pic the picture to associate to the given activity
        activityImage = findViewById(R.id.profilePic_guide);
        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { choosePicture(); }
        });

        // set actions to perform when the button btnManageChildren is clicked
        btnManageChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), activity_settings_managechildren.class));
                finish();
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


    // The following functions are called when the user clicks the button for changing the image picture

    // These functions open the gallery and allow the user to select his profile picture
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityGuideResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityGuideResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        activityImage.setImageURI(imageUri);
                        uploadPicture();
                    }
                }
            });

    // Support function that allows to update the given field of the given collection
    public static <T> void updateDatabaseField(FirebaseFirestore db, String collectionName,  String idDocument, String nameField, T value) {
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
                        Log.d("TAG", "Error update document", e);
                    }
                });
    }

    // This function uploads the image in the Firebase Storage folder of the user and updates the "photoURL" field in the database,
    // using the function define above
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        StorageReference ref = storageReference.child("images/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + auth.getCurrentUser().getUid());

        pd.setTitle("Uploading Image..");
        pd.show();

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateDatabaseField(db,"user", auth.getCurrentUser().getUid(),"photoURL", uri.toString());
                                Toast.makeText(activity_settings.this, "Caricamento nel db avvenuto correttamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_settings.this, "Errore nel caricamento nel db", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }
}