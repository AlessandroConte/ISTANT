package com.example.istant;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.istant.databinding.FragmentActivitiesBinding;
import com.example.istant.model.Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_activities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_activities extends Fragment {
    FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_activities() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_attivita.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_activities newInstance(String param1, String param2) {
        fragment_activities fragment = new fragment_activities();
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

        FragmentActivitiesBinding binding;

        db = FirebaseFirestore.getInstance();
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Activity> activities = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String nameActivity = document.getData().get("nameActivity").toString();
                                String address = document.getData().get("address").toString();
                                Timestamp dateStart = document.getTimestamp("dateStart");
                                Timestamp dateEnd = document.getTimestamp("dateEnd");
                                String description = document.getData().get("description").toString();
                                List<String> personInCharge = (List<String>)(document.get("personInCharge"));
                                String photoEvent = (String)(document.get("photoEvent"));

                                Activity activity = new Activity(id, nameActivity, address, dateStart, dateEnd, description, personInCharge, photoEvent);
                                activities.add(activity);
                            }
                            //binding = FragmentActivitiesBinding.inflate(inflater, container, false);
                            ListAdapter listAdapter  = new ListAdapter(getActivity(),activities);
                            //binding.listview.setAdapter(listAdapter);
                            //binding.listview.setClickable(true);

                            //data shared with next activity
                            /*
                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent i = new Intent(getActivity(), activity_visualizeactivities.class);
                                    i.putExtra("name",activities.get(position).getNameActivity());
                                    i.putExtra("phone",activities.get(position).getAddress());
                                    i.putExtra("country",activities.get(position).getId());
                                    i.putExtra("imageid",activities.get(position).getAddress());
                                    startActivity(i);
                                }
                            });

                             */
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_activities);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), activity_create_activities.class);
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        //return binding.getRoot();
        return null;
    }
}