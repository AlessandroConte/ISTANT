package com.example.istant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.istant.model.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_activities#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_activities extends Fragment{

    private ListView activitieslistview;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayAdapter<Activity> adapter;
    private ArrayList<Activity> activityArrayList;
    private Button newActivity;
    private ProgressDialog pd;
    private Context context;

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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        adapter = new ActivityAdapter(context, new ArrayList<Activity>());
        activityArrayList = new ArrayList<Activity>();
        activitieslistview = rootView.findViewById(R.id.listView_fragmentactivities);

        activitieslistview.setAdapter(adapter);
        activitieslistview.setClickable(true);

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

        db.collection("activity").
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    String name = document.get("nameActivity").toString();
                    String address = document.get("address").toString();
                    Timestamp dateStart = document.getTimestamp("dateStart");
                    Timestamp dateEnd = document.getTimestamp("dateEnd");
                    String description = document.get("description").toString();
                    // List<String> personInCharge = document.get("personInCharge").toString(); TODO: risolvere
                    String photo = document.get("photoEvent").toString();

                    Activity activity = new Activity(id, name, address, dateStart, dateEnd, description, null, photo);
                    activityArrayList.add(activity);

                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                adapter.clear();
                adapter.addAll(activityArrayList);
            }
        });

        activitieslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), activity_visualizeactivities.class);
                intent.putExtra("activity", activityArrayList.get(i));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private class ActivityAdapter extends ArrayAdapter<Activity> {
        ArrayList<Activity> activities;

        public ActivityAdapter(@NonNull Context context, ArrayList<Activity> activities) {
            super(context, 0, activities);
            this.activities = activities;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_activity, parent, false);
            }

            TextView activityName = convertView.findViewById(R.id.listadapter_activityName);
            TextView activityDescription = convertView.findViewById(R.id.listadapter_activityDescription);
            // de.hdodenhof.circleimageview.CircleImageView activityImage = convertView.findViewById(R.id.listadapter_activityPicture);

            Activity activity = activities.get(position);
            activityName.setText(activity.getNameActivity());
            activityDescription.setText(activity.getDescription());
            // Glide.with(getActivity()).load(Uri.parse(activity.getPhotoEvent())).into(activityImage); TODO: da implementare

            return convertView;
        }
    }
}