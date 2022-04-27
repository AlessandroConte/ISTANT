package com.example.istant;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.istant.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_visualizeusers extends AppCompatActivity {

    // Variables needed to change the picture of the activity
    private ImageView profilePic_visualizeuser;
    private DocumentReference documentReference;
    private FirebaseFirestore db;

    // variable needed to retrieve the intent
    private User user;

    private TextView tv_name;
    private TextView tv_surname;
    private TextView tv_phonenumber;
    private TextView tv_fiscalcode;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_dateofbirth; // TODO: fix

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizeusers);

        user = getIntent().getParcelableExtra("user");

        tv_name = findViewById(R.id.visualizeuser_edittext_name);
        tv_surname = findViewById(R.id.visualizeuser_edittext_surname);
        tv_phonenumber = findViewById(R.id.visualizeuser_edittext_phonenumber);
        tv_fiscalcode = findViewById(R.id.visualizeuser_edittext_fiscalcode);
        tv_address = findViewById(R.id.visualizeuser_edittext_address);
        tv_email = findViewById(R.id.visualizeuser_edittext_email);

        profilePic_visualizeuser = findViewById(R.id.profilePic_visualizeuser);

        tv_address.setText(user.getAddress());
        tv_email.setText(user.getEmail());
        tv_fiscalcode.setText(user.getFiscalCode());
        tv_name.setText(user.getName());
        tv_surname.setText(user.getSurname());
        Glide.with(activity_visualizeusers.this).load(user.getPhotoUrl()).into(profilePic_visualizeuser);
        tv_phonenumber.setText(user.getTelephoneNumber());

        // TODO: fixare i bug

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("User Guide"); // actionbar's name
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
}
