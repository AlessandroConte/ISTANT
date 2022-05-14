package com.example.istant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.example.istant.model.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass containing the list of the created activities
 */
public class ActivitiesFragment extends Fragment{

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // GUI
    private ListView activitieslistview;
    private ArrayAdapter<Activity> adapter;
    private ArrayList<Activity> activityArrayList;
    private Button newActivity;
    private ProgressDialog pd;
    private Context context;
    private Switch switchMyActivities;
    private SwipeRefreshLayout refreshLayout;

    // other
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // CONSTRUCTOR
    public ActivitiesFragment() {}

    // METHODS
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

        View rootView = inflater.inflate(R.layout.activities_fragment, container, false);
        context = container.getContext();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        adapter = new ActivityAdapter(context, new ArrayList<Activity>());
        newActivity = rootView.findViewById(R.id.fragmentActivities_btnNewLoan);
        switchMyActivities = rootView.findViewById(R.id.switch_activities);
        refreshLayout = rootView.findViewById(R.id.swipeRefresh_fragmentActivities);
        activityArrayList = new ArrayList<Activity>();
        activitieslistview = rootView.findViewById(R.id.listView_fragmentactivities);

        activitieslistview.setAdapter(adapter);
        activitieslistview.setClickable(true);

        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage(getString(R.string.fragmentactivities_loading));

        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, DefaultActivity.class));
            }
        });

        pd.show();
        displayActivities();

        switchMyActivities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    adapter.clear();
                    activityArrayList.clear();
                    displayMyActivities();

                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            adapter.clear();
                            activityArrayList.clear();
                            displayMyActivities();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
                else {
                    adapter.clear();
                    activityArrayList.clear();
                    displayActivities();

                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            adapter.clear();
                            activityArrayList.clear();
                            displayActivities();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                activityArrayList.clear();
                displayActivities();
                refreshLayout.setRefreshing(false);
            }
        });

        activitieslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), VisualizeActivitiesActivity.class);
                intent.putExtra("activity", activityArrayList.get(i));
                startActivity(intent);
            }
        });

        return rootView;
    }


    // this method is used to display the created activities
    private void displayActivities () {
        db.collection("activity")
                .orderBy("dateEnd", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    String name = document.get("nameActivity").toString();
                    String address = document.get("address").toString();
                    Timestamp dateStart = document.getTimestamp("dateStart");
                    Timestamp dateEnd = document.getTimestamp("dateEnd");
                    String description = document.get("description").toString();
                    List<String> personInCharge = (List<String>) document.get("personInCharge");
                    String photo = document.get("photoEvent").toString();

                    if (dateEnd.toDate().after(new Date())) {
                        Activity activity = new Activity(id, name, address, dateStart, dateEnd, description, personInCharge, photo);
                        activityArrayList.add(activity);
                    }

                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                adapter.clear();
                adapter.addAll(activityArrayList);
            }
        });
    }

    // this method is used to display the activities created by the logged user
    private void displayMyActivities () {
        db.collection("activity")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    String name = document.get("nameActivity").toString();
                    String address = document.get("address").toString();
                    Timestamp dateStart = document.getTimestamp("dateStart");
                    Timestamp dateEnd = document.getTimestamp("dateEnd");
                    String description = document.get("description").toString();
                    List<String> personInCharge = (List<String>) document.get("personInCharge");
                    String photo = document.get("photoEvent").toString();

                    if ((dateEnd.toDate().equals(new Date())||dateEnd.toDate().after(new Date())) && personInCharge.contains(auth.getCurrentUser().getUid())) {
                        Activity activity = new Activity(id, name, address, dateStart, dateEnd, description, personInCharge, photo);
                        activityArrayList.add(activity);
                    }
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
                adapter.clear();
                adapter.addAll(activityArrayList);
            }
        });
    }

    // Adapter for the ListView
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

            Activity activity = activities.get(position);
            activityName.setText(activity.getNameActivity());
            activityDescription.setText(activity.getDescription());

            return convertView;
        }
    }
}