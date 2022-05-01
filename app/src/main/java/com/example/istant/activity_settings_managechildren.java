package com.example.istant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.istant.model.Child;
import com.example.istant.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class activity_settings_managechildren extends AppCompatActivity {

    // TODO: da modificare

    ListView listview;
    ArrayList<User> userArrayList;
    Child child;

    private ListView childrenlistview;
    private FirebaseFirestore db;
    private ArrayAdapter<Child> adapter;
    private EditText searchBox;
    private Context context;
    private ArrayList<Child> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_managechildren);

        // This allows to the actionbar to have a back button pointing to the last activity you visited
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Manage Children");
        }

        // LIST OF INSTRUCTIONS NEEDED TO VIEW THE LIST OF CHILDREN

        // here I get the reference to the list view I created in the xml file
        listview = findViewById(R.id.settings_managechildren_childrenlist);

        // here is the data I'm going to feed the list, all contained in the userArrayList
        String[] name = {"Anthony","Leonard","Lucas","Albert","Mike","Michael","Toa","Ivana","Nicholas"};
        String[] phoneNo = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};
        userArrayList = new ArrayList<>();
        for(int i = 0;i< name.length;i++){
            //User user = new User(name[i],"","",phoneNo[i],"",R.drawable.ic_user);
            //userArrayList.add(user);
        }

        // Now I'm going to declare the adapter.
        // The adapter is a Java class that allows us to view a list of "complex" objects. These objects are formed by multiple elements.
        //listadapter_user listadapter = new listadapter_user( getApplicationContext(), userArrayList );

        // now we associate the listadapter with the listview
        //listview.setAdapter( listadapter );


        db = FirebaseFirestore.getInstance();
        childrenlistview = findViewById(R.id.settings_managechildren_childrenlist);


        adapter = new ChildrenAdapter(this, new ArrayList<Child>());
        childrenlistview.setAdapter(adapter);
        childrenlistview.setClickable(true);
        children = new ArrayList<Child>();

        db.collection("child")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp dateBorn = document.getTimestamp("bornDate");
                                int gender = Integer.parseInt(document.getData().get("gender").toString());
                                String name = document.getData().get("name").toString();
                                String surname = document.getData().get("surname").toString();

                                Child child = new Child("", null, dateBorn, gender, "", name, surname, "");
                                children.add(child);

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        adapter.clear();
                        adapter.addAll(children);
                    }
                });


        Button newChild = findViewById(R.id.btn_createchildren);
        newChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_settings_managechildren.this, activity_createNewChild.class);
                startActivity(intent);
            }
        });

    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class ChildrenAdapter extends ArrayAdapter<Child> {
        ArrayList<Child> children;

        public ChildrenAdapter(@NonNull Context context, ArrayList<Child> children) {
            super(context, 0, children);
            this.children = children;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_children,parent, false);
            }

            TextView childName = convertView.findViewById(R.id.listadapter_childName);
            TextView childSurname = convertView.findViewById(R.id.listadapter_childSurname);
            TextView childGender = convertView.findViewById(R.id.listadapter_childGender);
            TextView childAge = convertView.findViewById(R.id.listadapter_childDate);

            Child child = children.get(position);

            childName.setText(child.getName());
            childSurname.setText(child.getSurname());
            if(child.getGender() == 1){
                childGender.setText("M");
            }
            else{
                childGender.setText("F");
            }
            childAge.setText(child.getDateBorn().toString());

            return convertView;
        }
    }
}