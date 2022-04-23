package com.example.istant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.example.istant.model.Loan;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class activity_guide extends AppCompatActivity{

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