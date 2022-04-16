package com.example.istant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.istant.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Needed to show the list of users
    ListView listview;
    ArrayList<User_1> userArrayList;


    ListView l;
    String users_list[] = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

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
        /*
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        l = rootView.findViewById(R.id.search_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, users_list);
        l.setAdapter(arr);

        return rootView;

         */

        db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        EditText inputSearch;
                        Button buttonSearch;
                        String inputString;

                        buttonSearch = (Button) getView().findViewById(R.id.search_buttonSearch);
                        inputSearch = (EditText) getView().findViewById(R.id.et_cercaUtente);
                        ArrayList<User> users = new ArrayList<>();
                        buttonSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // inputString = inputSearch.getText().toString();
                                // TODO: fix
                            }
                        });
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String address = document.getData().get("address").toString();
                                Timestamp dateBorn = document.getTimestamp("dateBorn");
                                String email = document.getData().get("email").toString();
                                String fiscalCode = document.getData().get("fiscalCode").toString();

                                int gender;
                                if(Boolean.parseBoolean(document.getData().get("gender").toString())){
                                    gender = 1;
                                }
                                else {
                                    gender = 0;
                                }

                                String photoUrl = document.getData().get("photoURL").toString();
                                String name = document.getData().get("name").toString();
                                String surname = document.getData().get("surname").toString();
                                String telephoneNumber = document.getData().get("telephoneNumber").toString();

                                User user = new User(id, address, dateBorn, email, fiscalCode, gender, photoUrl, name, surname, telephoneNumber);
                                users.add(user);
                            }

                            // Now I'm going to declare the adapter.
                            // The adapter is a Java class that allows us to view a list of "complex" objects. These objects are formed by multiple elements.
                            listadapter_user listadapter = new listadapter_user(getActivity(), users);

                            // now we associate the list adapter with the listview
                            listview.setAdapter(listadapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // LIST OF INSTRUCTIONS NEEDED TO VIEW THE LIST OF PARTICIPANTS TO THE EVENT

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // here I get the reference to the list view I created in the xml file
        listview = rootView.findViewById(R.id.search_list);

        return rootView;
    }
}