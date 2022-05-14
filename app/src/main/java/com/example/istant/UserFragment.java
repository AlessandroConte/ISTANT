package com.example.istant;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass containing all the info oh the logged user
 */
public class UserFragment extends Fragment {

    // Firebase
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // GUI
    private ImageView profilePic_userfragment;
    private TextView tv_name;
    private TextView tv_surname;
    private TextView tv_phonenumber;
    private TextView tv_fiscalcode;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_dateofbirth;
    private RadioButton rb_sex_m;
    private RadioButton rb_sex_f;
    private Button btnModify;
    private Button btnManageChildren;

    // other
    private Uri imageUri;
    private String photoURL;
    private String name;
    private String surname;
    private String phoneNumber;
    private String fiscalCode;
    private String address;
    private String email;
    private String mParam1;
    private String mParam2;
    private boolean flag;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // CONSTRUCTOR
    public UserFragment() {}

    // METHODS

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.user_fragment, container, false);

        if (savedInstanceState == null) {
            getParentFragmentManager()
                    .beginTransaction()
                    //.replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        /**ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Profilo");
        }**/

        // retrieving the different fields of the gui
        tv_name = view.findViewById(R.id.fragmentUser_edittext_name);
        tv_surname = view.findViewById(R.id.fragmentUser_edittext_surname);
        tv_phonenumber = view.findViewById(R.id.fragmentUser_edittext_phonenumber);
        tv_fiscalcode = view.findViewById(R.id.fragmentUser_edittext_fiscalcode);
        tv_address = view.findViewById(R.id.fragmentUser_edittext_address);
        tv_email = view.findViewById(R.id.fragmentUser_edittext_email);
        rb_sex_m = view.findViewById(R.id.fragmentUser_genderradiobutton_m);
        rb_sex_f = view.findViewById(R.id.fragmentUser_genderradiobutton_f);
        tv_dateofbirth = view.findViewById(R.id.fragmentUser_edittext_dateofbirth);

        // Setting the Firebase variables
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("user").document(auth.getCurrentUser().getUid());


        // Retrieving the two buttons
        btnModify = view.findViewById(R.id.fragmentUser_buttonModify);
        btnManageChildren = view.findViewById(R.id.fragmentUser_buttonManageChildren);


        // Retrieving the ImageView
        profilePic_userfragment = view.findViewById(R.id.profilePic_fragmentUser);
        profilePic_userfragment.setClickable(false);


        // Setting all the EditText with the field of the DB
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String surname = documentSnapshot.get("surname").toString();
                String phoneNumber = documentSnapshot.get("telephoneNumber").toString();
                String fiscalCode = documentSnapshot.get("fiscalCode").toString();
                String address = documentSnapshot.get("address").toString();
                String email = documentSnapshot.get("email").toString();
                Timestamp bornDate = (Timestamp) documentSnapshot.get("dateBorn");
                Long gender = (Long) documentSnapshot.get("gender");

                String time = String.valueOf(bornDate.getSeconds());
                long timestampLong = Long.parseLong(time)*1000;
                Date d = new Date(timestampLong);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int date = c.get(Calendar.DATE);

                tv_name.setText(name);
                tv_surname.setText(surname);
                tv_phonenumber.setText(phoneNumber);
                tv_fiscalcode.setText(fiscalCode);
                tv_address.setText(address);
                tv_email.setText(email);
                tv_dateofbirth.setText(String.valueOf(date).concat(" - ").concat(String.valueOf(month + 1)).concat(" - ").concat(String.valueOf(year)));

                if (gender == 0){
                    rb_sex_m.toggle();
                }
                else {
                    rb_sex_f.toggle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firebase error", e.toString());
            }
        });


        // Getting the "photoURL" field of the current user
        // If the user has a profile picture, i.e. "photoURL" != "", then set it in the ImageView
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                photoURL = documentSnapshot.get("photoURL").toString();
                if (!photoURL.equals("")) {
                    Glide.with(UserFragment.this).load(photoURL).into(profilePic_userfragment);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Errore:" ,"Url non ottenuto");
            }
        });


        // Now we handle the buttons and the ImageView

        // ModifyButton
        // if the bool "flag" is:
        // false -> the textfields aren't editable and only show the data present at the given moment in the db. The button_modifysave has as text "modify"
        // true -> the textfields are editable and the user can change the fields. The button has as text "save"
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // false - it enables the field and the user can modify them, at the end it puts the flag as true
                if (!flag) {
                    btnModify.setText("Salva");

                    profilePic_userfragment.setClickable(true);
                    tv_name.setEnabled(true);
                    tv_surname.setEnabled(true);
                    tv_phonenumber.setEnabled(true);
                    tv_fiscalcode.setEnabled(true);
                    tv_address.setEnabled(true);
                    tv_address.setEnabled(true);
                    tv_email.setEnabled(true);
                    rb_sex_m.setEnabled(true);
                    rb_sex_f.setEnabled(true);

                    // ProfilePicture
                    profilePic_userfragment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            choosePicture();
                        }
                    });

                    flag = true;
                }
                // true - the user modified the field/s and now wants to save. It saves the modified values, it disables the fileds and then sets the falg to false.
                else {
                    // save the values contained in the fields in the activity info
                    btnModify.setText("Modifica");
                    // Save changes
                    name = tv_name.getText().toString();
                    surname = tv_surname.getText().toString();
                    phoneNumber = tv_phonenumber.getText().toString();
                    fiscalCode = tv_fiscalcode.getText().toString();
                    address = tv_address.getText().toString();
                    email = tv_email.getText().toString();

                    Map<String,Object> user = new HashMap<>();
                    user.put("address", address);
                    user.put("email", email);
                    user.put("fiscalCode", fiscalCode);
                    user.put("name", name);
                    user.put("surname", surname);
                    user.put("telephoneNumber", phoneNumber);

                    db.collection("user").document(auth.getCurrentUser().getUid())
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Success", "Campi aggiornati in modo corretto");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Firebase error ", e.toString());
                        }
                    });
                    // disable
                    tv_name.setEnabled(false);
                    tv_surname.setEnabled(false);
                    tv_phonenumber.setEnabled(false);
                    tv_fiscalcode.setEnabled(false);
                    tv_address.setEnabled(false);
                    tv_address.setEnabled(false);
                    tv_email.setEnabled(false);
                    rb_sex_m.setEnabled(false);
                    rb_sex_f.setEnabled(false);
                    flag = false;
                }
            }
        });

        // ManageChildren
        btnManageChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), SettingsManageChildrenActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    // This method allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in this activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // These functions open the gallery and allow the user to select his profile picture
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragmentUserResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> fragmentUserResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        profilePic_userfragment.setImageURI(imageUri);
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
    // using the function defined above
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        StorageReference ref = storageReference.child("profilePictures/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + "/" + auth.getCurrentUser().getUid());

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
                                updateDatabaseField(db,"user", auth.getCurrentUser().getUid(),"photoURL", uri.toString());
                                Toast.makeText(getActivity(), getString(R.string.fragmentuser_loading), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.fragmentuser_loadingerror), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.fragmentuser_imageuploadedcorrectly),Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.fragmentuser_loadingerror), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage( getString(R.string.fragmentuser_percentage) + ": " + (int) progressPercent + "%");
                    }
                });
    }

}