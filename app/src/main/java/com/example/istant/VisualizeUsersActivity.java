package com.example.istant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

/**
 *  This activity allows the user to visualize the info of the other users
 */
public class VisualizeUsersActivity extends AppCompatActivity {

    // Firebase
    private DocumentReference documentReference;
    private FirebaseFirestore db;

    // GUI
    private TextView tv_name;
    private TextView tv_surname;
    private TextView tv_phonenumber;
    private TextView tv_fiscalcode;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_dateofbirth;
    private RadioButton rb_m;
    private RadioButton rb_f;
    private ImageView profilePic_visualizeuser;
    private AlertDialog.Builder builder;

    // other
    private User user;

    // METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizeusers_activity);

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
            Glide.with(VisualizeUsersActivity.this).load(user.getPhotoUrl()).into(profilePic_visualizeuser);
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
            actionBar.setTitle(getString(R.string.activityvisualizeusers_actionbarname)); // actionbar's name
        }
    }

    // This method allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in this activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method that checks if there is internet connection
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
