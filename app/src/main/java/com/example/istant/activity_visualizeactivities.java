package com.example.istant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.is.istant.model.Child;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class activity_visualizeactivities extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_activity);

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Insert activity name here..."); // actionbar's name
        }
/*
        ListView participantsList = findViewById(R.id.visualizeactivity_participantslist);


        List<String> allergies = new ArrayList<String>();
        allergies.add("nuts");
        Child c = new Child("idalberto",allergies, Timestamp.valueOf("2018-09-01 09:01:16"), 1, "info child...", "Chris", "McDonald", "My parent" );
        Child[] cpart = {c,c,c};



        @SuppressLint("ResourceType") ArrayAdapter<Child> participants = new ArrayAdapter<Child>(this, R.id.activityparticipants_list , cpart);
*/




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