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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;

public class activity_visualizeactivities extends AppCompatActivity {
    private Button button_visualizeParticipants;
    private Button button_participate;
    private Button button_modifysave;
    private Button button_delete;

    // variable needed to make the fields editable.
    private boolean flag;

    // Retrieveing all of the fileds of the gui in order to enable and disable the edit options
    private TextView tv_where;
    private TextView tv_when;
    private EditText et_description;

    // Variables needed to change the picture of the activity
    private ImageView activityImage;
    private Uri imageUri;
    private String photoURL;

    // Firebase variables
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private FirebaseFirestore db;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_activity);


        // retrieve the "visualize participants" button from the gui & set an action on the button
        // the button, when pressed, sends the user to the list of the participants of the activity
        button_visualizeParticipants = findViewById( R.id.visualizeactivity_buttonViewParticipants );
        button_visualizeParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), activity_visualizeactivities_visualizeparticipants.class));
                finish();
            }
        });

        // retrieve the "delete" button from the gui and set the onClickListener
        button_delete = findViewById(R.id.visualizeactivity_buttondelete);
        // WE NEED TO SEE IF THE USER THAT IS OPENING THIS SCREEN IS THE CREATER OF THIS ACTIVITY
        // IF SO WE NEED TO SET THIS BUTTON VISIBLE
        //button_delete.setVisibility(View.INVISIBLE);  // THIS LINE OF CODE MAKES THE BUTTON INVISIBLE
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DELETE THE ACTIVITY!!!
            }
        });


        // retrieve the "participate" button from the gui and setting the listener
        button_participate = findViewById(R.id.visualizeactivity_buttonParticipate);
        button_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD ACTIONS TO PARTICIPATE !!!
            }
        });

        // retrieving the different fields of the gui
        tv_where = findViewById(R.id.visualizeactivities_edittext_where);
        tv_when = findViewById(R.id.visualizeactivities_edittext_when);
        et_description = findViewById(R.id.visualizeactivities_description);

        // Firebase variables
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // TODO: cambiare documentReference prendendo collection "activity" e documento dell'activity selezionata
        documentReference = db.collection("user").document(auth.getCurrentUser().getUid());


        // Here I am retrievenig the ImageView and setting an action when this is executed
        // The actions allows the user to change/pic the picture to associate to the given activity
        activityImage = findViewById(R.id.activityImage);

        // Getting the "photoEvent" field of the selected activity
        // If the activity has an image, i.e. "photoEvent" != "", then set it in the ImageView
        // TODO: da cambiare, perchè ora prende l'immagine profilo dell'utente corrente, noi vogliamo quella dell'attività
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                photoURL = documentSnapshot.get("photoURL").toString();
                if (!photoURL.equals("")) {
                    Glide.with(activity_visualizeactivities.this).load(photoURL).into(activityImage);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Errore:" ,"Url non ottenuto");
            }
        });

        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { choosePicture(); }
        });

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Insert activity name here..."); // actionbar's name
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
    public static <T> void updateDatabaseField(FirebaseFirestore db, String collectionName, String idDocument, String nameField, T value) {
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
    // TODO: da modificare anche qua, perchè aggiorna il campo di "user", noi vogliamo aggiornare quello di "activity"
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
                                Toast.makeText(activity_visualizeactivities.this, "Caricamento nel db avvenuto correttamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_visualizeactivities.this, "Errore nel caricamento nel db", Toast.LENGTH_SHORT).show();
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