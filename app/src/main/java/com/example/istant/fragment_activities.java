package com.example.istant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.istant.model.Activity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_activities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_activities extends Fragment implements ListAdapter_activities.OnActivityListener{

    private RecyclerView recyclerView;
    private ArrayList<Activity> activityArrayList;
    private ListAdapter_activities adapterActivities;
    private FirebaseFirestore db;
    private ProgressDialog pd;
    private Context context;

    private Button newActivity;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);
        context = container.getContext();

        activityArrayList = new ArrayList<Activity>();
        adapterActivities = new ListAdapter_activities(context, activityArrayList, this);

        recyclerView = rootView.findViewById(R.id.recyclerView_fragmentActivities);
        recyclerView.setAdapter(adapterActivities);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Manage the button to create a new activity
        newActivity = rootView.findViewById(R.id.fragmentActivities_btnNewLoan);
        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, activity_createNewActivities.class));
            }
        });


        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Fetching data..");
        pd.show();

        // FragmentActivitiesBinding binding;

        db = FirebaseFirestore.getInstance();

        db.collection("activity").orderBy("dateStart", Query.Direction.DESCENDING)

                /*
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
                            binding = FragmentActivitiesBinding.inflate(inflater, container, false);
                            ListAdapter_activities listAdapterActivities = new ListAdapter_activities(getActivity(),activities);
                            binding.listview.setAdapter(listAdapter);
                            binding.listview.setClickable(true);

                            data shared with next activity

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
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
                 */

        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    Log.d("Firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        activityArrayList.add(dc.getDocument().toObject(Activity.class));
                    }
                    adapterActivities.notifyDataSetChanged();
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                }
            }
        });

        /*
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

         */

        return rootView;
    }

    @Override
    public void onActivityClick(int position) {
        Log.d("RecyclerView item", "position = " + position);
        Intent intent = new Intent(context, activity_visualizeactivities.class);
        // intent.putExtra("activity", activityArrayList.get(position));
        // TODO: implementare il putExtra
        startActivity(intent);
    }
}