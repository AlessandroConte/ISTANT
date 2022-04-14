package com.example.istant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
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

public class activity_guide extends AppCompatActivity {

    private ImageView profilePic_guide;
    private Button choosePic;
    private Uri imageUri;
    private String photoURL;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__guide);

        /*

        profilePic_guide = findViewById(R.id.profilePic_guide);
        choosePic = findViewById(R.id.button_pic);
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("user").document(auth.getCurrentUser().getUid());

        // Getting the "photoURL" field of the current user
        // If the user has a profile picture, i.e. "photoURL" != "", then set it in the ImageView
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                photoURL = documentSnapshot.get("photoURL").toString();
                if (!photoURL.equals("")) {
                    Glide.with(activity_guide.this).load(photoURL).into(profilePic_guide);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Errore:" ,"Url non ottenuto");
            }
        });

        // Listener of the button
        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

         */

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("User Guide"); // actionbar's name
        }
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
                        profilePic_guide.setImageURI(imageUri);
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
                                Toast.makeText(activity_guide.this, "Caricamento nel db avvenuto correttamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_guide.this, "Errore nel caricamento nel db", Toast.LENGTH_SHORT).show();
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

}