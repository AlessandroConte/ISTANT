package com.example.istant;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_search extends Fragment {
    private ListView userslistview;
    private FirebaseFirestore db;
    private ArrayAdapter<User> adapter;
    private EditText searchBox;
    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_cerca.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_search newInstance(String param1, String param2) {
        fragment_search fragment = new fragment_search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        db = FirebaseFirestore.getInstance();
        userslistview = rootView.findViewById(R.id.users_listView);
        searchBox = rootView.findViewById(R.id.et_searchUser);

        adapter = new UserAdapter(context, new ArrayList<User>());
        userslistview.setAdapter(adapter);

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

        return rootView;
    }

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
}