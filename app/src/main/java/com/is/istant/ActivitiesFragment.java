package com.is.istant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView l;
    private String activitiesNames[] = {
            "Prestito 1",
            "Prestito 2",
            "Prestito 3",
            "Prestito 4"
    };

    private Integer imageid[] = {
            R.drawable.gr_info,
            R.drawable.ic_user,
            R.drawable.gr_info,
            R.drawable.ic_user

    };

    private String activitiesRates[] = {
            "Prestito 1",
            "Prestito 2",
            "Prestito 3",
            "Prestito 4"
    };



    public ActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesFragment newInstance(String param1, String param2) {
        ActivitiesFragment fragment = new ActivitiesFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        /*
        l = rootView.findViewById(R.id.activities_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, activities_list);
        l.setAdapter(arr); */

        ListView listView=(ListView)rootView.findViewById(android.R.id.list);

        CustomActivitiesList customActivitiesList = new CustomActivitiesList(getActivity(), activitiesNames, activitiesRates, imageid);
        listView.setAdapter(customActivitiesList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity().getApplicationContext(),"You Selected "+ " as Country",Toast.LENGTH_SHORT).show();        }
        });

        return rootView;
    }
}