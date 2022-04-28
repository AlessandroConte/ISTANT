package com.example.istant;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.istant.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;

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
    private TextView tv_dateofbirth;
    private RadioButton rb_m;
    private RadioButton rb_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizeusers);

        user = getIntent().getParcelableExtra("user");
        db = FirebaseFirestore.getInstance();

        tv_name = findViewById(R.id.visualizeuser_edittext_name);
        tv_surname = findViewById(R.id.visualizeuser_edittext_surname);
        tv_phonenumber = findViewById(R.id.visualizeuser_edittext_phonenumber);
        tv_fiscalcode = findViewById(R.id.visualizeuser_edittext_fiscalcode);
        tv_address = findViewById(R.id.visualizeuser_edittext_address);
        tv_email = findViewById(R.id.visualizeuser_edittext_email);
        tv_dateofbirth = findViewById(R.id.visualizeuser_edittext_dateofbirth);
        rb_m = findViewById(R.id.visualizeuser_genderradiobutton_m);
        rb_f = findViewById(R.id.visualizeuser_genderradiobutton_f);
        profilePic_visualizeuser = findViewById(R.id.profilePic_visualizeuser);

        String id = user.getId();
        documentReference = db.collection("user").document(id);

        tv_address.setText(user.getAddress());
        tv_email.setText(user.getEmail());
        tv_fiscalcode.setText(user.getFiscalCode());

        if (user.getGender() == 0){
            rb_m.toggle();
        }
        else {
            rb_f.toggle();
        }

        if (!user.getPhotoUrl().equals("")) {
            Glide.with(activity_visualizeusers.this).load(user.getPhotoUrl()).into(profilePic_visualizeuser);
        }

        tv_name.setText(user.getName());
        tv_surname.setText(user.getSurname());
        tv_phonenumber.setText(user.getTelephoneNumber());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Timestamp bornDate = (Timestamp) documentSnapshot.get("dateBorn");
                String time = String.valueOf(bornDate.getSeconds());
                long timestampLong = Long.parseLong(time)*1000;
                Date d = new Date(timestampLong);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int date = c.get(Calendar.DATE);

                tv_dateofbirth.setText(String.valueOf(date).concat(" - ").concat(String.valueOf(month + 1)).concat(" - ").concat(String.valueOf(year)));
            }
        });

        // TODO: gender

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Profilo utente"); // actionbar's name
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
