package com.example.istant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.istant.model.User;

import java.util.ArrayList;

public class activity_visualizeactivities_visualizeparticipants extends AppCompatActivity {
    ListView listview;
    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizeactivities_visualizeparticipants);

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Activity participants"); // actionbar's name
        }

        // LIST OF INSTRUCTIONS NEEDED TO VIEW THE LIST OF PARTICIPANTS TO THE EVENT

        // here I get the reference to the list view I created in the xml file
        listview = findViewById(R.id.visualizeactivity_visualizeparticipants_listparticipants);

        // here is the data I'm going to feed the list, all contained in the userArrayList
        String[] name = {"Anthony","Leonard","Lucas","Albert","Mike","Michael","Toa","Ivana","Nicholas"};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        userArrayList = new ArrayList<>();
        for(int i = 0;i< name.length;i++){
            //User_1 user = new User_1(name[i],"","",phoneNo[i],"",R.drawable.ic_user);
            //userArrayList.add(user);
        }

        // Now I'm going to declare the adapter.
        // The adapter is a Java class that allows us to view a list of "complex" objects. These objects are formed by multiple elements.
        //listadapter_user listadapter = new listadapter_user( getApplicationContext(), userArrayList );

        // now we associate the listadapter with the listview
        //listview.setAdapter( listadapter );

    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), activity_visualizeactivities.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}