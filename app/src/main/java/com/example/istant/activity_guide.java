package com.example.istant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.istant.model.Loan;
import com.example.istant.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class activity_guide extends AppCompatActivity{

    // TODO: questa è una prova di quella che sarà poi l'activity_visualizeuser

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


    /*
    private RecyclerView recyclerView;
    private ArrayList<Loan> loanArrayList;
    private NewListAdapter_loans adapterLoans;
    FirebaseFirestore db;
    ProgressDialog pd;

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__guide);

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
        Glide.with(activity_guide.this).load(user.getPhotoUrl()).into(profilePic_visualizeuser);
        tv_phonenumber.setText(user.getTelephoneNumber());

        // TODO: fixare i bug


        /*
        loanArrayList = new ArrayList<Loan>();
        adapterLoans = new NewListAdapter_loans(activity_guide.this, loanArrayList,this);

        recyclerView = findViewById(R.id.recyclerView_guide);
        recyclerView.setAdapter(adapterLoans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Fetching data..");
        pd.show();

        db = FirebaseFirestore.getInstance();

        db.collection("loan").orderBy("dateStart", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                            Log.d("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                loanArrayList.add(dc.getDocument().toObject(Loan.class));
                            }
                            adapterLoans.notifyDataSetChanged();
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                        }
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

    /*
    @Override
    public void onLoanClick(int position) {
        Intent intent = new Intent(this, activity_visualizeloans.class);
        //intent.putExtra("loan", loanArrayList.get(position)); // TODO: quando invochiamo la nuova schermata, dobbiamo portarci le info del prestito cliccato
        startActivity(intent);
        Log.d("RecyclerView Item","position = " + position);
    }
     */
}