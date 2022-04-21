package com.example.istant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.istant.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class activity_guide extends AppCompatActivity {

    /*
    private FirebaseFirestore db;
    private ListView usersListView;
    private ArrayAdapter<User> adapter;
    private EditText searchBox;

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__guide);

        /*
        db = FirebaseFirestore.getInstance();
        usersListView = findViewById(R.id.guide_listView);
        searchBox = findViewById(R.id.searchBox);

        adapter = new UserAdapter(this, new ArrayList<User>());
        usersListView.setAdapter(adapter);

        db.collection("user")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<User> users = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String id = document.getId();
                                    String address = document.getData().get("address").toString();
                                    Timestamp dateBorn = document.getTimestamp("dateBorn");
                                    String email = document.getData().get("email").toString();
                                    String fiscalCode = document.getData().get("fiscalCode").toString();
                                    int gender = Integer.parseInt(document.getData().get("gender").toString());
                                    String photoUrl = document.getData().get("photoURL").toString();
                                    String name = document.getData().get("name").toString();
                                    String surname = document.getData().get("surname").toString();
                                    String telephoneNumber = document.getData().get("telephoneNumber").toString();

                                    User user = new User(id, address, dateBorn, email, fiscalCode, gender, photoUrl, name, surname, telephoneNumber);
                                    users.add(user);
                                }
                                adapter.clear();
                                adapter.addAll(users);
                            }
                        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("FIRESTORE - SEARCH", "Searchbox has changed to: " + editable.toString());

                if (editable.toString().isEmpty()){
                    db.collection("user")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    ArrayList<User> users = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String id = document.getId();
                                        String address = document.getData().get("address").toString();
                                        Timestamp dateBorn = document.getTimestamp("dateBorn");
                                        String email = document.getData().get("email").toString();
                                        String fiscalCode = document.getData().get("fiscalCode").toString();
                                        int gender = Integer.parseInt(document.getData().get("gender").toString());
                                        String photoUrl = document.getData().get("photoURL").toString();
                                        String name = document.getData().get("name").toString();
                                        String surname = document.getData().get("surname").toString();
                                        String telephoneNumber = document.getData().get("telephoneNumber").toString();

                                        User user = new User(id, address, dateBorn, email, fiscalCode, gender, photoUrl, name, surname, telephoneNumber);
                                        users.add(user);
                                    }
                                    adapter.clear();
                                    adapter.addAll(users);
                                }
                            });
                }
                else {
                    db.collection("user")
                            .whereEqualTo("name", editable.toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    ArrayList<User> users = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String id = document.getId();
                                        String address = document.getData().get("address").toString();
                                        Timestamp dateBorn = document.getTimestamp("dateBorn");
                                        String email = document.getData().get("email").toString();
                                        String fiscalCode = document.getData().get("fiscalCode").toString();
                                        int gender = Integer.parseInt(document.getData().get("gender").toString());
                                        String photoUrl = document.getData().get("photoURL").toString();
                                        String name = document.getData().get("name").toString();
                                        String surname = document.getData().get("surname").toString();
                                        String telephoneNumber = document.getData().get("telephoneNumber").toString();

                                        User user = new User(id, address, dateBorn, email, fiscalCode, gender, photoUrl, name, surname, telephoneNumber);
                                        users.add(user);
                                    }
                                    adapter.clear();
                                    adapter.addAll(users);
                                }
                            });
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

    /*
    private class UserAdapter extends ArrayAdapter<User> {
        ArrayList<User> users;

        public UserAdapter(@NonNull Context context, ArrayList<User> users) {
            super(context, 0, users);
            this.users = users;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item,parent, false);
            }

            TextView userName = convertView.findViewById(R.id.itemName);
            TextView userPhoneNumber = convertView.findViewById(R.id.itemTelephoneNumber);

            User user = users.get(position);
            userName.setText(user.getName());
            userPhoneNumber.setText(user.getTelephoneNumber());

            return convertView;
        }
    }

     */

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