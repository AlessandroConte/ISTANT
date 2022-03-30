package com.example.istant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

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
        int[] imageId = {R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,
                R.drawable.ic_user, R.drawable.ic_user,R.drawable.ic_user};
        String[] name = {"Attivitàaaaaaaaaaaa1","Uscita al parcooooooooooooooooooo","Nuoto","Calcio pazzo in via delle rive ","Mike","Michael","Toa","Ivana"," "};
        String[] description = {"Descrizione","Andiamo al parco alle 5","Passaggio nuoto ore 3","Trasporto verso campo da calcio ore 6","prova",
                "i'm in meeting","Gotcha","Let's Go","RIGA DA SVUOTARE PER BELLEZZA"};
        String[] reviews = {"3.0/5","5.0/5","4.2/5","3.7/5","1.0/5",
                "3.0/5","2.7/5","4.5/5"," "};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        String[] country = {"United States","Russia","India","Israel","Germany","Thailand","Canada","France","Switzerland"};
        userArrayList = new ArrayList<>();
        for(int i = 0;i< imageId.length;i++){
            User user = new User(name[i],description[i],"",phoneNo[i],country[i],imageId[i]);
            userArrayList.add(user);
        }

        // Now I'm going to declare the adapter.
        // The adapter is a Java class that allows us to view a list of "complex" objects. These objects are formed by multiple elements.
        ListAdapter listadapter = new ListAdapter( getApplicationContext(), userArrayList );

        // now we associate the listadapter with the listview
        listview.setAdapter( listadapter );

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