package com.example.istant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.istant.model.Loan;

public class activity_visualizeloans extends AppCompatActivity {


    private ImageView image_loan;
    private EditText loan_description;
    private EditText loan_startDate;
    private EditText loan_endDate;

    private Button button_modify;
    private Button button_partecipate;
    private Button button_delete;

    // variable needed to retrieve the intent
    private Loan loan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizeloans);

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Insert loan name here..."); // actionbar's name
        }

        loan = getIntent().getParcelableExtra("loan");

        image_loan = findViewById(R.id.visualizeloans_image);
        loan_description = findViewById(R.id.visualizeloans_edittext_description);
        loan_startDate = findViewById(R.id.visualizeloans_edittext_datestart);
        loan_endDate = findViewById(R.id.visualizeloans_edittext_dateend);
        button_modify = findViewById(R.id.visualizeloans_buttonModifySave);
        button_delete = findViewById(R.id.visualizeloans_buttonDelete);
        button_partecipate = findViewById(R.id.visualizeloans_buttonParticipate);

        // Getting the information from the clicked item
        // TODO: mancano dateStart e dateEnd
        if (!loan.getPhotoLoan().equals("")){
            Glide.with(this).load(loan.getPhotoLoan()).into(image_loan);
        }
        loan_description.setText(loan.getDescription());

        // TODO: implementare l'onClick dei bottoni
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